package com.chen.behindimagesmanage.controller;

import com.chen.behindimagesmanage.annotation.CurrentDirectLinkToken;
import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import com.chen.behindimagesmanage.service.PicGoService;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.Base64ToMultipartFile;
import com.chen.behindimagesmanage.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.Buffer;

/**
 * @author 15031
 */
@Slf4j
@Controller
@RequestMapping("/api/picgo")
public class PicGoController {
    @Resource
    private PicGoService picGoService;

    @PostMapping("/upload")
    @ResponseBody
    public ApiResponse<Object> upload(String imageName, MultipartFile file, @CurrentDirectLinkToken DirectLinkToken directLinkToken){

       return picGoService.storeUpload(file, file.getOriginalFilename(), directLinkToken);
    }
    @GetMapping("/viewImg")
    public ResponseEntity<org.springframework.core.io.Resource> upload(String md5MetaFileName, @CurrentDirectLinkToken DirectLinkToken directLinkToken){
        return picGoService.viewImg(md5MetaFileName,directLinkToken);
    }

}
