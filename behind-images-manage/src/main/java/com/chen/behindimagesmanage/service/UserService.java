package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.annotation.CurrentUserUuid;
import com.chen.behindimagesmanage.model.FolderModel;
import com.chen.behindimagesmanage.pojo.Folder;
import com.chen.behindimagesmanage.pojo.PageFiles;
import com.chen.behindimagesmanage.pojo.User;
import com.chen.behindimagesmanage.util.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 15031
 */
public interface UserService {
    /**
     * 获取首页相关的日志信息
     * @param userUuid 用户uuid
     * @return 首页数据
     */
    ApiResponse<Object> getHomeLog(String userUuid);
    /**
     * 上传存储图片
     * @param file 文件
     * @param path 路径
     * @param userUuid 用户id
     * @return 简单回复
     */
    ApiResponse<String> storeImg (MultipartFile file, String path, String userUuid);

    /**
     * 文件夹
     * @param path 路径
     * @param userUuid 用户id
     * @return 简单回复
     */
    ApiResponse<String> createFolder(String path, String userUuid);

    /**
     * 获取用户路径所有文件
     * @param path 路径
     * @param userUuid 用户id
     * @return 文件目录打包对象
     */
    ApiResponse<PageFiles> getPageFiles(String path, String userUuid);

    /**
     * 删除文件或目录
     * @param path 路径
     * @param userUuid 用户id
     * @return 简单回复
     */
    ApiResponse<String> deleteFile(String path, String userUuid);

    /**
     * 下载img
     * @param fileName 文件名
     * @param uuid 用户id
     * @return 返回文件流
     */
    ResponseEntity<Resource> downImg(String fileName, String uuid);

    /**
     * 返回图片
     * @param metaFileName md5文件名
     * @param uuid 用户id
     * @return 返回行内图片二进制流
     */
    ResponseEntity<Resource>getImg(String metaFileName, @CurrentUserUuid String uuid);

    /**
     * 包括重命名
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @param uuid 用户id
     * @return 简单回复
     */
    @Transactional(rollbackFor = RuntimeException.class)
    ApiResponse<String> mvFiles(String oldPath, String newPath,String uuid);

    ApiResponse<List<Folder>>getSpecFolders(String userUuid, String specPath);

    ApiResponse<FolderModel>getFolderModel(String userUuid);

    ApiResponse<User> putAvatar(MultipartFile avatar,String userUuid);

    ApiResponse<String> putPassword(String newPassword,String userUuid);


}

