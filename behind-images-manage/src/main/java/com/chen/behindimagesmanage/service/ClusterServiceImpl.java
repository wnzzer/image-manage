package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.dao.FileDao;
import com.chen.behindimagesmanage.exception.NotFindSourceException;
import com.chen.behindimagesmanage.pojo.ImageMetaData;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author 15031
 */
@Service
@RequiredArgsConstructor
public class ClusterServiceImpl implements ClusterService {

    private FileDao fileDao;

    @Override
    public ResponseEntity<byte[]> packageMultipleFiles()  {
        byte[]file = new byte[0];
        try {
            file = FileUtil.readFileToByteArray(FileUtil.zipDirectory(FileUtil.IMG_FOLDER_PATH,"img.zip"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "images.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @Override
    public ResponseEntity<Resource> getImg(String md5FileName)  {
        byte[]file = new byte[0];
        String filePath = FileUtil.IMG_FOLDER_PATH+"/"+md5FileName;
        Path localPath = Paths.get(filePath);


        if (Files.exists(localPath)) {

            // 创建 FileSystemResource
            Resource fileResource = new FileSystemResource(localPath.toFile());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + localPath.getFileName().toString())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);
        } else {
            // 文件不存在时返回 404
            throw new NotFindSourceException("无法找到资源");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResponse<String> receiveImg(MultipartFile file,String metaFileName)  {
        Path path = Paths.get(FileUtil.IMG_FOLDER_PATH+"/"+metaFileName);
        if (Files.exists(path)){
            return ApiResponse.success();
        }else {
            try {
                file.transferTo(path);
                ImageMetaData imageMetaData = null;
                List<String>localUrlList = null;
                String[]filaPartName = FileUtil.splitFileNameAndExtension(metaFileName);
                if(filaPartName != null){
                    imageMetaData = fileDao.getImg(filaPartName[0]);
                }
                if(imageMetaData != null){
                   localUrlList  = imageMetaData.getLocalUrl();
                }
                if(localUrlList != null && !localUrlList.contains(PodDataSynConfig.CURRENT_POD)){
                    localUrlList.add(PodDataSynConfig.CURRENT_POD);
                    fileDao.updateLocalUrl(imageMetaData);
                    return ApiResponse.success();
                }

                return ApiResponse.success();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

    }
    @Override
    public ApiResponse<Boolean>getSyStatus(){
        return ApiResponse.success(PodDataSynConfig.syedFlag);
    }
}
