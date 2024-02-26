package com.chen.behindimagesmanage.dao;

import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import com.chen.behindimagesmanage.pojo.Folder;
import com.chen.behindimagesmanage.pojo.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 15031
 */
@Mapper
public interface UserDao {
    /**
     * 存入图片
     * @param image 图片信息
     * @return int 影响数量
     */
    @Insert("INSERT INTO image (image_name, folder_uuid, user_uuid, image_size, thumbnail_data, md5) " +
            "SELECT #{image.imageName}, #{image.folderUuid}, #{image.userUuid}, #{image.imageSize}, #{image.thumbnailData}, #{image.md5} " +
            "FROM DUAL " +
            "WHERE NOT EXISTS (SELECT 1 FROM image WHERE user_uuid = #{image.userUuid}" +
            " AND folder_uuid = #{image.folderUuid} AND image_name = #{image.imageName})")
    int insertImg(@Param("image") Image image);

    /**
     * 查询文件夹
     * @param path 路径
     * @param userUuid 用户id
     * @return
     */
    @Select("SELECT * FROM folder WHERE  path = #{path} AND user_uuid = #{userUuid}")
    Folder selectFolder(@Param("path")String path, @Param("userUuid")String userUuid);


    /**
     * 查询该路径目录
     * @param parentFolderUuid 父文件夹
     * @param userUuid 用户uuid
     * @return
     */
    @Select("SELECT * FROM folder WHERE  parent_folder_uuid = #{path} AND user_uuid = #{userUuid}")
    List<Folder> selectFolders(@Param("path")String parentFolderUuid, @Param("userUuid")String userUuid);

    /**
     * 查询该路径图片
     * @param folderUuid 路径文件夹的uuid
     * @param userUuid 路径文件夹的用户id
     * @return
     */
    @Select("SELECT * FROM image WHERE  folder_uuid = #{folderUuid} AND user_uuid = #{userUuid}")
    List<Image> selectImgs(@Param("folderUuid")String folderUuid, @Param("userUuid")String userUuid);

    /**
     * 查询用户的图片
     * @param md5 md5
     * @param userUuid 路径文件夹的用户id
     * @return 图片对象
     */
    @Select("SELECT * FROM image WHERE  md5 = #{md5} AND user_uuid = #{userUuid}")
    Image selectImg(@Param("md5")String md5,@Param("userUuid")String userUuid);

    /**
     * 查询用户的图片
     * @param imgName 文件名
     * @param userUuid 路径文件夹的用户id
     * @return 图片对象
     */
    @Select("SELECT COUNT(*)  FROM image WHERE  image_name = #{imgName} AND user_uuid = #{userUuid}")
    int selectExist(@Param("imgName")String imgName,@Param("userUuid")String userUuid);


    /**
     * 查询图片
     * @param imgName 文件名
     * @param parentFolderUuid 父目录id
     * @param userUuid 用户id
     * @return 图片
     */
    @Select("SELECT * FROM image WHERE  image_name = #{imgName} AND folder_uuid = #{pUuid} AND user_uuid = #{userUuid}")
    Image selectImgByImgName(@Param("imgName")String imgName,@Param("pUuid") String parentFolderUuid, @Param("userUuid")String userUuid);

    /**
     * 插入目录
     * @param uuid 文件目录uuid
     * @param folderName 目录名
     * @param path 路径
     * @param parentFolderUuid 父id
     * @param userUuid 用户id
     * @return int 影响行
     */

    @Insert("INSERT INTO folder (uuid, folder_name, path, parent_folder_uuid, user_uuid) " +
            "VALUES (#{uuid}, #{folderName}, #{path}, #{parentFolderUuid}, #{userUuid})")
    int insertFolder(@Param("uuid")String uuid,@Param("folderName")String folderName,@Param("path")String path,@Param("parentFolderUuid")String parentFolderUuid,@Param("userUuid") String userUuid);


    /**
     * 删除用户目录
     * @param folderUuid 目录uuid
     * @return int 影响行
     */
    @Delete("DELETE FROM folder WHERE uuid = #{folderUuid}")
    int deleteFolder(@Param("folderUuid") String folderUuid);

    /**
     * 删除用户图片
     * @param folderUuid 目录uuid
     * @param imageName 图片名
     * @param userUuid 用户id
     * @return int 影响行
     */
    @Delete("DELETE FROM image WHERE folder_uuid = #{folderUuid} AND image_name = #{imageName} AND user_uuid = #{userUuid}")
    int deleteImg(@Param("folderUuid") String folderUuid,@Param( "imageName") String imageName,@Param("userUuid") String userUuid);

    /**
     * 删除目录文件
     * @param folderUuid 目录id
     * @param userUuid 用户id
     * @return
     */
    @Delete("DELETE FROM image WHERE folder_uuid = #{folderUuid}  AND user_uuid = #{userUuid}")
    int deleteImgByFolder(@Param("folderUuid") String folderUuid,@Param("userUuid") String userUuid);

    /**
     * 更新父目录和名字
     * @param image 新的image
     * @return 影响行数
     */
    @Update("UPDATE image SET image_name = #{imageName},folder_uuid = #{folderUuid}  WHERE id = #{id}")
    int updateImgPathOrName(Image image);

    /**
     * 更新路径和名字
     * @param folder 新的文件信息对象
     * @return
     */
    @Update("UPDATE folder SET folder_name = #{folderName},path = #{path}  WHERE id = #{id}")
    int updateFolderPathOrName(Folder folder);

    /**
     * 查询用户picgo token
     * @param userUuid 用户uuid
     * @return picgo token对象
     */
    @Select("SELECT * from direct_link_tokens WHERE user_uuid = #{userUuid}")
    DirectLinkToken selectPicgoToken(@Param("userUuid") String userUuid);


    @Insert("INSERT into direct_link_tokens (link_token, store_path, user_uuid, version) VALUES" +
            " (#{newLinkToken},'/',#{userUuid},0)")
    int insertPicgoToken (@Param("newLinkToken") String newLinkToken, @Param("userUuid") String userUuid);

    int putPicgoToken (DirectLinkToken directLinkToken);


    @Update("UPDATE  user SET  thumbnail_avatar = #{thumbnailAvatar} WHERE uuid = #{uuid}")
    int putAvatar(@Param("thumbnailAvatar") byte[] thumbnailAvatar,@Param("uuid") String uuid);

    @Update("UPDATE user SET  password = #{password} WHERE uuid = #{uuid}")
    int putPassword(@Param("password") String newPassword,@Param("uuid") String uuid);




}
