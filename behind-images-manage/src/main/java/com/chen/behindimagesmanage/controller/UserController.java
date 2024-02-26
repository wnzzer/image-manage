package com.chen.behindimagesmanage.controller;

import com.chen.behindimagesmanage.annotation.CurrentDirectLinkToken;
import com.chen.behindimagesmanage.annotation.CurrentUserUuid;
import com.chen.behindimagesmanage.model.FolderModel;
import com.chen.behindimagesmanage.pojo.*;
import com.chen.behindimagesmanage.service.CountService;
import com.chen.behindimagesmanage.service.PicGoService;
import com.chen.behindimagesmanage.service.UserService;
import com.chen.behindimagesmanage.service.UserServiceImpl;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.TokenUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 15031
 */
@Slf4j
@RequestMapping("/api/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private TokenUtil tokenUtil;
    @Resource
    private PicGoService picGoService;
    @GetMapping("/getHomeData")
    public ApiResponse<Object> getHomeData(@CurrentUserUuid String userUuid){
        return userService.getHomeLog(userUuid);
    }

    @PostMapping("/uploadImg")
    public ApiResponse<String> uploadImg(@RequestParam("file")  MultipartFile file, @RequestParam("path")String path,HttpServletRequest request,@CurrentUserUuid String uuid){
        log.info(uuid+"用户发送了文件路径："+path+"文件:=>"+file);
        return  userService.storeImg(file,path,uuid);
    }
    @PostMapping("/createFolder")
    public ApiResponse<String> createFolder(@RequestParam("newPath") String newPath,@CurrentUserUuid String uuid){
        log.info(uuid+"请求创建"+newPath);
        return  userService.createFolder(newPath,uuid);
    }
    @GetMapping("/getPageFiles")
    public ApiResponse<PageFiles> getPageFiles(@RequestParam("path") String path, @CurrentUserUuid String uuid){
        log.info(uuid+"请求获取"+path+"文件");
        return userService.getPageFiles(path, uuid);
    }
    @GetMapping("/getImg")
    public ResponseEntity<org.springframework.core.io.Resource> getImg(String metaFileName, @CurrentUserUuid String uuid){
        log.info(uuid+"请求获取"+metaFileName+"文件");
        return userService.getImg(metaFileName, uuid);

    }

    @GetMapping("/downImg")
    public ResponseEntity<org.springframework.core.io.Resource> downFiles(String md5MetaFileName, @CurrentUserUuid String uuid){
        log.info(uuid+"请求获取下载"+md5MetaFileName+"文件");
        return userService.downImg(md5MetaFileName,uuid);
    }
    @DeleteMapping("/deleteFiles")
    public ApiResponse<String> deleteFiles(@RequestParam("path") String path, @CurrentUserUuid String uuid){
        log.info(uuid+"请求获取删除"+path+"文件");
        return userService.deleteFile(path,uuid);
    }

    @PutMapping("/mvFiles")
    public ApiResponse<String> mvFiles(@RequestParam("oldPath") String oldPath, @RequestParam("newPath") String newPath,@CurrentUserUuid String uuid){
        log.info("oldPath=>"+oldPath);
        log.info("newPath=>"+newPath);
        return userService.mvFiles(oldPath, newPath, uuid);
    }

    @GetMapping("/getPicgoToken")
    public ApiResponse<DirectLinkToken>getPicgoToken(@CurrentUserUuid String userUuid){
        return picGoService.getPicgoToken(userUuid);
    }

    @PutMapping("/putPicgoToken")
    public ApiResponse<DirectLinkToken>putPicgoDirectLinkToken(@CurrentUserUuid String userUuid,@NonNull @RequestBody DirectLinkToken directLinkToken){
        directLinkToken.setUserUuid(userUuid);
        return picGoService.putPicgoToken(directLinkToken,userUuid);
    }
    @GetMapping("/getSpecFolders")
    public ApiResponse<List<Folder>>getSpecFolders(@CurrentUserUuid String userUuid, @NonNull String specPath){
        return userService.getSpecFolders(userUuid,specPath);
    }

    @GetMapping("/getAllFolders")
    public ApiResponse<FolderModel>getAllFolders(@CurrentUserUuid String userUuid){
        return userService.getFolderModel(userUuid);

    }

    @PutMapping("/putPassword")
    public ApiResponse<String>putPassword(@CurrentUserUuid String userUuid, String newPassword){
        return userService.putPassword(newPassword,userUuid);

    }

    @PutMapping("/putAvatar")
    public ApiResponse<User>putAvatar(@CurrentUserUuid String userUuid, MultipartFile avatar){
        return userService.putAvatar(avatar,userUuid);

    }





}

