package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.util.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 15031
 */
public interface ClusterService {
    /**
     * 打包所有图片文件文件
     * @return zip字节流
     */
    ResponseEntity<byte[]> packageMultipleFiles();

    /**
     * 获取md5图片
     * @param md5FileName md5图片文件名
     * @return 返回文件展示链接
     */
    ResponseEntity<Resource> getImg(String md5FileName);

    /**
     * 接受图片
     * @param file 文件
     * @return 返回
     */
    ApiResponse<String> receiveImg(MultipartFile file,String metaFileName);

    ApiResponse<Boolean>getSyStatus();
}
