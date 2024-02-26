package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.dao.UserDao;
import com.chen.behindimagesmanage.exception.ExceptionEnum;
import com.chen.behindimagesmanage.pojo.*;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.FileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author 15031
 */
@Service
public class PicGoServiceImpl implements PicGoService {
    @Resource
    private UserTranInterface tranInterface;
    @Resource
    private UserDao userDao;
    @Resource
    private  UserService userService;
    @Resource
    private  RedisService redisService;

    @Override
    public ApiResponse<Object> storeUpload( MultipartFile file, String fileName, DirectLinkToken directLinkToken) {
        String rootPath = "/";
        String rootFolderUuid = "/";
        Folder folder = userDao.selectFolder(directLinkToken.getStorePath(), directLinkToken.getUserUuid());
        if(rootPath.equals(directLinkToken.getStorePath()) || folder != null){
            byte[] bytes = FileUtil.getThumbnailBaos(file);
            String md5 = FileUtil.getFileMd5(file);
            String size = FileUtil.countFileSizetoString(file.getSize());
            Image image = new Image(null,fileName,
                    folder==null?rootFolderUuid:folder.getUuid(),directLinkToken.getUserUuid(),size,bytes,md5,null,null);
            ImageMetaData imageMetaData = new ImageMetaData(null,md5,null,new ArrayList<>(){{add(PodDataSynConfig.CURRENT_POD);}},0);
            ApiResponse<String> apiResponse = tranInterface.insertImg(image,imageMetaData,file);
            String[]fileNameParts = FileUtil.splitFileNameAndExtension(fileName);
            String md5MetaFileName = md5 + "."+fileNameParts[1];
            if(apiResponse.getStatusCode() == 200){
                return ApiResponse.success(new HashMap<>(){{put("md5MetaFileName",md5MetaFileName);}});
            }else {
                return ApiResponse.error(apiResponse.getStatusCode(),apiResponse.getData());
            }

        }
        return null;
    }
    @Override
    public  ResponseEntity<org.springframework.core.io.Resource> viewImg(String metaFileName, DirectLinkToken directLinkToken){
        return userService.getImg(metaFileName,directLinkToken.getUserUuid());
    }

    @Override
    public ApiResponse<DirectLinkToken> getPicgoToken(String userUuid) {
        DirectLinkToken directLinkToken = userDao.selectPicgoToken(userUuid);
        String picgoToken = null;

        if(directLinkToken == null || directLinkToken.getLinkToken().length() < 32){
            picgoToken = UUID.randomUUID().toString();
            int result = userDao.insertPicgoToken(picgoToken,userUuid);
            if(result >= 1){
                directLinkToken = userDao.selectPicgoToken(userUuid);
            }else {
                return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
            }

        }else {
            picgoToken = directLinkToken.getLinkToken();

        }
        redisService.insertPicGoToken(picgoToken,directLinkToken);

        return ApiResponse.success(directLinkToken);



    }
    @Override
    public ApiResponse<DirectLinkToken> putPicgoToken(DirectLinkToken directLinkToken,String userUuid){
        String newPicGoToken = UUID.randomUUID().toString();
        if(directLinkToken.getLinkToken() != null){
            directLinkToken.setLinkToken(newPicGoToken);
        }
        DirectLinkToken lastdirectLinkToken = userDao.selectPicgoToken(directLinkToken.getUserUuid());
        String lastPicoGoToken = lastdirectLinkToken.getLinkToken();
        redisService.deletePicGoToken(lastPicoGoToken);

        if(userDao.putPicgoToken(directLinkToken) >= 1){

            return ApiResponse.success(userDao.selectPicgoToken(userUuid));
        }else {
            return ApiResponse.error(500,"修改失败");

        }
    }



}
