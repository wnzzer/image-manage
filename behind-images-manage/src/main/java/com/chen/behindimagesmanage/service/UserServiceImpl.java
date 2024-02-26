package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.annotation.CurrentUserUuid;
import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.dao.CountDao;
import com.chen.behindimagesmanage.dao.FileDao;
import com.chen.behindimagesmanage.dao.UserDao;
import com.chen.behindimagesmanage.exception.ExceptionEnum;
import com.chen.behindimagesmanage.exception.NotFindSourceException;
import com.chen.behindimagesmanage.exception.ParameterException;
import com.chen.behindimagesmanage.model.FolderModel;
import com.chen.behindimagesmanage.pojo.*;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.FileUtil;
import com.chen.behindimagesmanage.util.SyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static com.chen.behindimagesmanage.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;
import static com.chen.behindimagesmanage.exception.ExceptionEnum.Unprocessable_Entity;

/**
 * @author 15031
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private RedisService redisService;
    @Resource
    private UserDao userDao;
    @Resource
    private UserTranService userTranService;
    @Resource
    private CountService countService;
    @Resource
    private CountDao countDao;

    @Resource
    FileDao fileDao;
    @Resource
    private SyUtil syUtil;
    /**
     * 我把根文件夹的id设为为/",
     */
    String rootPath = "/";
    String rootFolderId = "/";
    int unitBase = 1024;

    @Override
    public ApiResponse<Object> getHomeLog(String userUuid){

        //获取总数信息
        UserLoginLog userLoginLog = countDao.selectLoginLog(userUuid);
        TotalStats totalStats = countDao.searchTotalCount(userUuid);
        List<TotalStats>sevenTotalStats = countDao.searchSevenTotalCount(userUuid);
        List<FileIncludeNameStatistics>newFileStatistics = countDao.searchFileIncludeNameLinkCount(userUuid);
        DeviceStatistics deviceStatistics = countDao.searchDevicesCount(userUuid);
        Map<String,Object>returnMap = new HashMap<>(10){{
            put("userLoginLog",userLoginLog);
            put("totalStats",totalStats);
            put("sevenTotalStats",sevenTotalStats);
            put("newFileStatistics",newFileStatistics);
            put("deviceStatistics",deviceStatistics);

        }};
        return ApiResponse.success(returnMap);

    }

    @Override
    public ApiResponse<String>storeImg(MultipartFile file, String path, String userUuid){
        redisService.clearPageFiles(path, userUuid);
        String fileSize = FileUtil.countFileSizetoString(file.getSize());
        if(!FileUtil.isFileSizeLessThan(file.getSize(),50, FileUtil.SizeUnit.MB)){
            return ApiResponse.error(Unprocessable_Entity,"文件不得大于50mb");
        }


        String fileName = file.getOriginalFilename();
        String md5 = FileUtil.getFileMd5(file);
        LocalDateTime uploadTime = LocalDateTime.now();

        Folder folder = new Folder();
        folder.setUuid(rootFolderId);
        if(!(rootPath).equals(path)){
            folder = userDao.selectFolder(path,userUuid);
            if(folder == null){
                return ApiResponse.error(Unprocessable_Entity);
            }
        }
        //如果没有改文件夹

        byte[] thumbnailBytes = FileUtil.getThumbnailBaos(file);
        Image image = new Image(null,fileName,folder.getUuid(),userUuid,fileSize,thumbnailBytes,md5,null,null);
        ImageMetaData imageMetaData = new ImageMetaData(null,md5,null, new ArrayList<>(){{add(PodDataSynConfig.CURRENT_POD);}},0);
        //发送文件同步给其他pod
        syUtil.sendFile(file,md5+"."+ Objects.requireNonNull(FileUtil.splitFileNameAndExtension(fileName))[1]);
        return userTranService.insertImg(image,imageMetaData,file);

    }



    @Override
    public ApiResponse<String> createFolder(String path, String userUuid){
        redisService.clearPageFiles(path, userUuid);
        String[]paths = path.substring(1).split("/");
        String folderName = paths[paths.length - 1];
        String parentUuid = rootFolderId;
        if(paths.length > 1){
            String backPath = rootFolderId + String.join("/", Arrays.copyOfRange(paths, 0, paths.length - 1));
            Folder folder =  userDao.selectFolder(backPath,userUuid);
            if(folder == null){
                return ApiResponse.error(Unprocessable_Entity);
            }
            parentUuid = folder.getUuid();
        }
        String folderUuid = UUID.randomUUID().toString();
        int count = userDao.insertFolder(folderUuid,folderName,path,parentUuid,userUuid);

        if(count >= 1){
            //清除缓存

            return ApiResponse.success("创建成功");
        }else {
            return ApiResponse.error(INTERNAL_SERVER_ERROR);
        }

    }
    @Override
    public ApiResponse<PageFiles> getPageFiles(String path, String userUuid){
        //获取缓存
        PageFiles cachedValue = null;
        try {
            cachedValue = redisService.selectPageFiles(path,userUuid);
        } catch (JsonProcessingException e) {
            log.error(userUuid+"获取目录缓存异常"+path);
            throw new RuntimeException(e);
        }

        if (cachedValue != null) {
            return ApiResponse.success(cachedValue);
        }
        String folderUuid = rootFolderId;
        if(!rootPath.equals(path)){
            Folder folder = userDao.selectFolder(path,userUuid);
            if (folder.getUuid() == null){
                return ApiResponse.error(Unprocessable_Entity);
            }else {
                folderUuid = folder.getUuid();
            }
        }
        List<Folder>folderList = userDao.selectFolders(folderUuid,userUuid);
        List<Image>imageList = userDao.selectImgs(folderUuid,userUuid);
        PageFiles pageFiles = new PageFiles(folderList,imageList);
        //存入缓存页面
        try {
            redisService.storePageFiles(path,pageFiles,userUuid);
        } catch (JsonProcessingException e) {
            log.error(userUuid+"存储目录数据缓存失败：json转换异常"+path);
        }
        return ApiResponse.success(pageFiles);
    }

    @Override
    public ApiResponse<String> deleteFile(String path, String userUuid){

        return userTranService.deleteFile(path,userUuid);

    }


    @Override
    public ResponseEntity<org.springframework.core.io.Resource> downImg(String md5MetaFileName, String uuid)  {
        String[]metaNameParts = FileUtil.splitFileNameAndExtension(md5MetaFileName);
        Image image = userDao.selectImg(metaNameParts[0],uuid);
        if(image == null){
            throw new NotFindSourceException("没有找到资源");
        }
        countService.addLinkCount(image.getId(),uuid);
        String filePath = FileUtil.IMG_FOLDER_PATH+"/"+ md5MetaFileName;
        Path localPath = Paths.get(filePath);
        if (Files.exists(localPath)) {

            // 创建 FileSystemResource
            org.springframework.core.io.Resource fileResource = new FileSystemResource(localPath.toFile());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + localPath.getFileName().toString())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);
        } else {
            // 文件不存在时返回 404
            syUtil.getMd5File(md5MetaFileName);
            try {
                return buildFileResponse(localPath,md5MetaFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource>getImg(String metaFileName, @CurrentUserUuid String uuid){
        try {
            String[] fileNameParts = metaFileName.split("\\.");
            System.out.println(fileNameParts[0]+uuid);
            Image image = userDao.selectImg(fileNameParts[0],uuid);
            if(image == null){
                throw new NotFindSourceException("没有该参数");
            }
            // 构建图片文件的路径
            Path imagePath = Paths.get(FileUtil.IMG_FOLDER_PATH).resolve(metaFileName);
            org.springframework.core.io.Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 构建响应头，设置Content-Disposition等
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + metaFileName);
                Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(metaFileName);

                return ResponseEntity.ok()
                        .contentType(mediaType.get())
                        .headers(headers)
                        .body(resource);
            } else {
                syUtil.getMd5File(metaFileName);
                return buildFileResponse(imagePath,metaFileName);

            }
        } catch (IOException e) {
            // 处理异常
            return ResponseEntity.notFound().build();
        }
    }
    private ResponseEntity<org.springframework.core.io.Resource> buildFileResponse(Path path, String filename) throws IOException {
        if (Files.exists(path)) {
            org.springframework.core.io.Resource fileResource = new FileSystemResource(path.toFile());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(filename);

            return ResponseEntity.ok()
                    .contentType(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .headers(headers)
                    .body(fileResource);
        } else {
            throw new NotFindSourceException("同步获取资源失败");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResponse<String> mvFiles(String oldPath, String newPath, String userUuid){
        redisService.clearPageFiles(oldPath,userUuid);
        PathInfo oldPathInfo = new PathInfo(oldPath);
        PathInfo newPathInfo = new PathInfo(newPath);
        Folder oldFolder = new Folder();
        Folder newFolder = new Folder();


        if (rootPath.equals(newPathInfo.getPrefixDirectory())){
            newFolder.setUuid(rootFolderId);
        }
        //如果是文件
        if(oldPathInfo.isFile()){
            //获取源文件
            //如果是根目录文件，特殊处理
            if(rootPath.equals(oldPathInfo.getPrefixDirectory())){
                oldFolder.setUuid(rootFolderId);
            }else {
                oldFolder = userDao.selectFolder(oldPathInfo.getPrefixDirectory(),userUuid);
            }
            Image originImage = userDao.selectImgByImgName(oldPathInfo.getFileName(),oldFolder.getUuid(),userUuid);
            if(originImage != null){
                //如果是根目录文件，特殊处理
                if(rootPath.equals(newPathInfo.getPrefixDirectory())){
                    newFolder.setUuid(rootFolderId);
                }else {
                    newFolder = userDao.selectFolder(newPathInfo.getPrefixDirectory(),userUuid);
                }
                originImage.setImageName(newPathInfo.getFileName());
                originImage.setFolderUuid(newFolder.getUuid());
                int count = userDao.updateImgPathOrName(originImage);
                if(count >= 1){
                    return ApiResponse.success();
                }else {
                    return ApiResponse.error(INTERNAL_SERVER_ERROR);
                }

            }else {
                throw new NotFindSourceException("没有该资源");
            }


        }
        //如果是目录
        else if(oldPathInfo.isDirectory()){
            String oldFolderPath = oldPathInfo.getPrefixDirectory() + (rootPath.equals(oldPathInfo.getPrefixDirectory())?
                    oldPathInfo.getDirectoryName() : "/" + oldPathInfo.getDirectoryName());
            String newFolderPath = newPathInfo.getPrefixDirectory() + (rootPath.equals(newPathInfo.getPrefixDirectory())?
                    newPathInfo.getDirectoryName() : "/" + newPathInfo.getDirectoryName());
            Folder folder = userDao.selectFolder(oldFolderPath,userUuid);
            if(folder == null){
                throw new NotFindSourceException("没有找到资源");
            }
            Folder newParentFolder = rootPath.equals(newPathInfo.getPrefixDirectory())?
                    new Folder(null,"/",null,null,null,null,null,null):userDao.selectFolder(newPathInfo.getPrefixDirectory(),userUuid);
            if(newParentFolder == null){
                throw new ParameterException("违规的新名称或者路径");
            }
            folder.setFolderName(newPathInfo.getDirectoryName());
            folder.setPath(newFolderPath);
            folder.setParentFolderUuid(newParentFolder.getParentFolderUuid());
            List<Folder>tempFolderList = userDao.selectFolders(folder.getUuid(),userUuid);
            LinkedList<Folder>folderLinkedList = new LinkedList<>(){{
                for(Folder tempList : tempFolderList){
                    offer(tempList);
                }
            }};

            userDao.updateFolderPathOrName(folder);

            folderLinkedList.offer(folder);
            while (!folderLinkedList.isEmpty()){
                Folder tempFolder = folderLinkedList.poll();
                //修改路径和文件名
                for(Folder addFolder : tempFolderList){
                    folderLinkedList.offer(addFolder);
                }
                String tempNewPath  = tempFolder.getPath().replace(oldFolderPath,newFolderPath);
                userDao.updateFolderPathOrName(tempFolder);
            }

        }
        return ApiResponse.success();

    }

    @Override
    public ApiResponse<List<Folder>> getSpecFolders(String userUuid, String specPath) {
        Folder folder = null;
        if(specPath.equals(rootPath)){
            folder = new Folder();
            folder.setUuid(rootFolderId);
        }else {
            userDao.selectFolder(userUuid, specPath);
        }
        if(folder != null){
            List<Folder>folderList = userDao.selectFolders(folder.getUuid(),userUuid);
            return ApiResponse.success(folderList);
        }else {
            return ApiResponse.error(Unprocessable_Entity);
        }
    }

    @Override
    public ApiResponse<FolderModel>getFolderModel(String userUuid){
        FolderModel folderModel = buildFolderModel("/","/","/",userUuid);
        return  ApiResponse.success(folderModel);



    }

    @Override
    public ApiResponse<User> putAvatar(MultipartFile avatar, String userUuid) {
        if(!FileUtil.isFileSizeLessThan(avatar.getSize(),5, FileUtil.SizeUnit.MB)){
            return ApiResponse.error(Unprocessable_Entity,"文件不得大于5mb");
        }
        byte[]thumbnailAvatar = FileUtil.getThumbnailBaos(avatar);
        if(userDao.putAvatar(thumbnailAvatar,userUuid) >= 1){
            User user = new User();
            user.setThumbnailAvatar(thumbnailAvatar);
            return ApiResponse.success(user);
        }else {
            ApiResponse.error(INTERNAL_SERVER_ERROR,"修改失败");
        }


        return null;
    }

    @Override
    public ApiResponse<String> putPassword(String newPassword, String userUuid) {
        if(userDao.putPassword(newPassword, userUuid) >= 1){
            return ApiResponse.success("修改成功");
        }else {
            return ApiResponse.error(INTERNAL_SERVER_ERROR,"修改失败");
        }
    }


    /**
     * 用于同步或者获取之后更新数据库文件的所有服务器
     */




    private FolderModel buildFolderModel(String path, String name,String parentFolderUuid, String userUuid) {
        List<Folder> folders = userDao.selectFolders(parentFolderUuid, userUuid);

        FolderModel folderModel = new FolderModel();
        if(folders != null){
            for (Folder folder : folders) {
                FolderModel childFolderModel = buildFolderModel(folder.getPath(),folder.getFolderName(),folder.getUuid(), userUuid);
                folderModel.getChildren().add(childFolderModel);
            }
        }
        folderModel.setValue(path);
        folderModel.setLabel(name);
        return folderModel;
    }



}
