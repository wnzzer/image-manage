package com.chen.behindimagesmanage.controller;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.service.ClusterService;
import com.chen.behindimagesmanage.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * @author 15031
 */
@Slf4j
@RestController
@RequestMapping("/k8s")
public class ClusterSyController {
    @Resource
    private ClusterService clusterService;

    @GetMapping("/getAllFiles")
    public ResponseEntity<byte[]> returnZipFiles(String token) {

            return clusterService.packageMultipleFiles();

    }

    @GetMapping("/getFile")
    public ResponseEntity<org.springframework.core.io.Resource> returnImg( String md5FileName) {

            return clusterService.getImg(md5FileName);

    }
    @PostMapping("/receiveImg")
    public ApiResponse<String> receiveImg(@RequestParam("file")MultipartFile file,String metaFileName) {

            return clusterService.receiveImg(file,metaFileName);

    }

    @GetMapping("/getSyStatus")
    public ApiResponse<Boolean> getSyStatus() {
        return clusterService.getSyStatus();

    }
}