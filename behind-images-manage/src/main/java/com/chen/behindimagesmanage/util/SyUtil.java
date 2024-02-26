package com.chen.behindimagesmanage.util;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.dao.FileDao;
import com.chen.behindimagesmanage.exception.OptimisticLockException;
import com.chen.behindimagesmanage.pojo.ImageMetaData;
import com.chen.behindimagesmanage.pojo.ImageMetaDataAddFileName;
import com.chen.behindimagesmanage.pojo.ScheduleTask;
import com.chen.behindimagesmanage.service.SyTaskServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

/**
 * @author 15031
 */
@Slf4j
@Component
public class SyUtil {
    private final PodUtil podUtil;
    HttpUtil httpUtil;
    FileDao fileDao;
    private final String serverPort;

    private final String dnsSuffix;

    private final String imgFolderPath = FileUtil.IMG_FOLDER_PATH;
    public List<String>allFileName = null;




    public SyUtil(PodUtil podUtil, HttpUtil httpUtil, Environment environment, FileDao fileDao,     @Value("${server.port}")
    String serverPort,
                  @Value("${config.k8s.headless-service-name}") String headLessServiceName,
                  @Value("${config.k8s.namespace}") String namespace,
                  List<String> allFileName) {
        this.podUtil = podUtil;
        this.httpUtil = httpUtil;
        this.fileDao = fileDao;
        this.serverPort = serverPort;
        this.allFileName = allFileName;
        this.dnsSuffix = String.format(".%s.%s.svc.cluster.local",headLessServiceName,namespace);
        httpUtil.dnsSuffix = dnsSuffix;
    }

    @Async
    public  void startSy(){
        log.info("开始同步文件");
        String bestPod = podUtil.getBestPod();
        log.info("寻找最优同步节点:"+bestPod);

        String fileUrl = httpUtil.questProtocol+bestPod+dnsSuffix+":"+serverPort+ "?token="+PodDataSynConfig.SY_KEY_VALUE;
        // Send an HTTP GET request to the file URL
        byte[] fileData = httpUtil.getBytes(fileUrl);
        if (fileData != null) {
            FileUtil.unZip(fileData,imgFolderPath);
        }else {
            log.warn("无法获取最优节点文件，准备进行数据库扫表同步");
      }
        //开始数据库同步
        partSy();
    }


    @Async
    public void partSy(){
        PodDataSynConfig.syedFlag = false;
        allFileName = FileUtil.listFilesInDirectory(imgFolderPath);
        log.info("开始扫表同步文件");
        List<ImageMetaDataAddFileName> imageMetaDataList = fileDao.getAllImg();
        if(imageMetaDataList != null){
            for(ImageMetaDataAddFileName imageMetaData : imageMetaDataList){
                if(!imageMetaData.getLocalUrl().contains(PodDataSynConfig.CURRENT_POD)){

                    Stack<String>stack = new Stack<>(){{
                        for(String pod :imageMetaData.getLocalUrl()){
                            push(pod);
                        }
                    }};
                    String metaFileName = imageMetaData.getMd5() + "." + FileUtil.splitFileNameAndExtension(imageMetaData.getImageName())[1];
                    byte[]array = accessServers(stack,metaFileName);
                    if(array != null){
                        File file = new File(imgFolderPath+"/"+metaFileName);
                        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                            fileOutputStream.write(array);
                            addPodRecord(new ImageMetaData(imageMetaData));
                        } catch (IOException e) {
                            log.warn("文件写入异常:=>"+imageMetaData.getMd5());
                            throw new RuntimeException();
                        }
                    }
                }

            }

        }
        PodDataSynConfig.syedFlag = true;
        //如果是第一次即没有初始化完成，就创建同步的定时任务
         


    }
    public boolean getMd5File(String md5FileName){
        podUtil.flushPod();
        Stack<String>stack = new Stack<>(){{
            for(String pod : podUtil.surPod){
                push(pod);
            }
        }};
        byte[]array = accessServers(stack,md5FileName);
        if(array != null){
            File file = new File(imgFolderPath+"/"+md5FileName);
            try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(array);
            } catch (IOException e) {
               return false;
            }
        }
        fileDao.updateAddLocalUrl(md5FileName,PodDataSynConfig.CURRENT_POD);
        return true;
    }


    public byte[] accessServers(Stack<String>stackPods,String md5FileName) {
        for (String server : stackPods) {
            try {
                String url = httpUtil.questProtocol + server+dnsSuffix + ":"+serverPort +  "/k8s/getFile?md5FileName="+md5FileName;

                // 尝试访问服务器
                return httpUtil.getBytes(url);
            } catch (Exception e) {
                // 服务器不可达，继续下一个服务器
            }
        }
        return null;
    }


    public void addPodRecord(List<ImageMetaData>imgMetaDataList) {
        for(ImageMetaData imageMetaData : imgMetaDataList){
            if(imageMetaData.getLocalUrl().contains(PodDataSynConfig.CURRENT_POD)) {
                continue;
            }

            imageMetaData.getLocalUrl().add(PodDataSynConfig.CURRENT_POD);
            updateImageMetaData(imageMetaData);

        }
    }
    public void addPodRecord(ImageMetaData imageMetaData) {
         if(!imageMetaData.getLocalUrl().contains(PodDataSynConfig.CURRENT_POD)){
             imageMetaData.getLocalUrl().add(PodDataSynConfig.CURRENT_POD);
             updateImageMetaData(imageMetaData);
         }

    }

    /**
     * 乐观锁重试更新localUrl处理
     * @param imageMetaData 请求参数
     */
    @Retryable(maxAttempts = 3, include = { OptimisticLockException.class })
    public void updateImageMetaData(ImageMetaData imageMetaData) {
            // 执行操作
            // 如果成功，不会进入catch块
            int row = fileDao.updateLocalUrl(imageMetaData);
            if(row == 0){
                throw new OptimisticLockException("乐观锁异常");
            }
    }
    @Async
    public void sendFile(MultipartFile file,String metaFileName){
        podUtil.flushPod();
        for(String pod : podUtil.getOthersPods()){
            String url = httpUtil.questProtocol +pod+dnsSuffix + ":" +serverPort +"/k8s/receiveImg";
            MultiValueMap<String, Object> body = null;
            try {
                body = getStringObjectMultiValueMap(file);
                body.add("metaFileName",metaFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            try {
                sendFile(url,requestEntity);
            } catch (Exception e) {
                log.error("推送到服务器url出错==>"+url);
            }
        }
    }

    private static MultiValueMap<String, Object> getStringObjectMultiValueMap(MultipartFile file) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        InputStreamResource resource = new InputStreamResource(file.getInputStream()) {
            @Override
            public String getFilename() {
                // 返回原始文件名，这对于某些接收方可能是必需的
                return file.getOriginalFilename();
            }

            @Override
            public long contentLength() {
                // 返回文件大小，这不是必须的，但对于某些服务器配置可能有帮助
                return file.getSize();
            }
        };
        body.add("file",resource);
        return body;
    }

    @Async
    @Retryable(maxAttempts = 3, include = { OptimisticLockException.class })
    public void sendFile(String url,HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        httpUtil.sendFormDataPostRequest(url,requestEntity);
    }










}
