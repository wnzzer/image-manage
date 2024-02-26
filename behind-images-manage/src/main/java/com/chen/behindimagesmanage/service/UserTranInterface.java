package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.pojo.Image;
import com.chen.behindimagesmanage.pojo.ImageMetaData;
import com.chen.behindimagesmanage.util.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 15031
 */
public interface UserTranInterface {
    /**
     * 图片存入事务
     * @param image 图片对象
     * @param imageMetaData 数据对象
     * @param file 文件
     * @return ApiResponse<String> 简单结果
     */
    ApiResponse<String> insertImg(Image image, ImageMetaData imageMetaData, MultipartFile file);

    /**
     * 删除文件或者目录
     * @param path 路径
     * @param userUuid id
     * @return
     */
    public ApiResponse<String> deleteFile(String path, String userUuid);
}
