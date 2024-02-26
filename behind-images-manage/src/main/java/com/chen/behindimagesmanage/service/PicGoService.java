package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.annotation.CurrentDirectLinkToken;
import com.chen.behindimagesmanage.annotation.CurrentUserUuid;
import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import com.chen.behindimagesmanage.pojo.Token;
import com.chen.behindimagesmanage.util.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author 15031
 */
@Service
public interface PicGoService {
    /**
     * 存储图床上床图片
     * @param file 上传的文件
     * @param fileName 文件名
     * @Param directLinkToken 用户直链对象
     * @return 返回url
     */
    ApiResponse<Object> storeUpload(MultipartFile file, String fileName, DirectLinkToken directLinkToken);

    ResponseEntity<Resource> viewImg(String metaFileName, DirectLinkToken directLinkToken);

    ApiResponse<DirectLinkToken> getPicgoToken(String userUuid);


    ApiResponse<DirectLinkToken> putPicgoToken(DirectLinkToken directLinkToken,String userUuid);



}
