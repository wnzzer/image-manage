package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.dao.CountDao;
import com.chen.behindimagesmanage.dao.FileDao;
import com.chen.behindimagesmanage.dao.UserDao;
import com.chen.behindimagesmanage.exception.OptimisticLockException;
import com.chen.behindimagesmanage.pojo.Folder;
import com.chen.behindimagesmanage.pojo.Image;
import com.chen.behindimagesmanage.pojo.ImageMetaData;
import com.chen.behindimagesmanage.pojo.PathInfo;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.chen.behindimagesmanage.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;

/**
 * @author 15031
 */
@Service
public class UserTranService implements UserTranInterface{

    @Resource
    private UserDao userDao;
    @Resource
    private FileDao fileDao;
    @Resource
    private CountService countService;
    @Resource
    private CountDao countDao;
    @Resource
    private RedisService redisService;
    public ApiResponse<String> insertImg(Image image, ImageMetaData imageMetaData, MultipartFile file){
        //上传日志
        countService.addUploadCount(image.getUserUuid());
        if (userDao.insertImg(image) >= 1){
            System.out.println("md5"+image.getMd5());
            ImageMetaData imageMetaData1 = fileDao.getImg(image.getMd5());
            if(imageMetaData1 != null){
                //存在md5
                if(imageMetaData1.getLocalUrl().contains(PodDataSynConfig.CURRENT_POD)){
                    return ApiResponse.success("success");
                }else {
                    imageMetaData1.getLocalUrl().add(PodDataSynConfig.CURRENT_POD);
                    fileDao.updateLocalUrl(imageMetaData1);
                }
            }else{
                if(fileDao.insertImageMetadata(imageMetaData) == 0){
                    throw new OptimisticLockException("乐观锁异常");
                }

                String originalFilename = file.getOriginalFilename();
                assert originalFilename != null;
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                try {
                    File file1 = new File(FileUtil.IMG_FOLDER_PATH+"/"+imageMetaData.getMd5()+fileExtension);
                    file.transferTo(file1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            return ApiResponse.success("上传成功");
        }else {
            return ApiResponse.error(INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResponse<String> deleteFile(String path, String userUuid) {
        countService.addDeleteCount(userUuid);
        redisService.clearPageFiles(path, userUuid);
        PathInfo pathInfo = new PathInfo(path);
        if(pathInfo.isFile()){
            String parentFolderUuid = "/";
            Folder folder = null;
            if(!pathInfo.getPrefixDirectory().equals(parentFolderUuid)){
                folder = userDao.selectFolder(pathInfo.getDirectoryName(),userUuid);
            }
            if(folder != null){
                parentFolderUuid = folder.getUuid();

            }
            countDao.deleteFileCount(parentFolderUuid);
            userDao.deleteImg(parentFolderUuid,pathInfo.getFileName(),userUuid);
            return ApiResponse.success("成功");


        }else {
            //删除目录
            Folder folder = userDao.selectFolder(path,userUuid);
            Queue<Folder> folders = new LinkedList<>();
            if(folder != null){
                folders.offer(folder);
            }
            //递归删除
            int count = 0;
            while(!folders.isEmpty()){
                Folder tempFolder = folders.poll();
                List<Folder> folderList = userDao.selectFolders(tempFolder.getUuid(),userUuid);
                for(Folder elementFolder : folderList){
                    folders.offer(elementFolder);
                }
                count+= userDao.deleteFolder(tempFolder.getUuid());
                countDao.deleteFileCount(tempFolder.getUuid());
                count+= userDao.deleteImgByFolder(tempFolder.getUuid(),userUuid);
            }
            return ApiResponse.success("删除了记录："+count);
        }
    }
}
