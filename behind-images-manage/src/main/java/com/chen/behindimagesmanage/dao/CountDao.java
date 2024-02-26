package com.chen.behindimagesmanage.dao;

import com.chen.behindimagesmanage.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 15031
 */
@Mapper
public interface CountDao {
    /**
     * 更新或者插入登录信息
     * @param userLoginLog 登录信息
     * @return 影响行
     */
    @Insert("INSERT INTO user_login_log (last_login_time, last_login_ip, last_login_device, user_uuid) " +
            "VALUES (NOW(), #{lastLoginIp}, #{lastLoginDevice}, #{userUuid}) " )
    int insertUserLoginLog(UserLoginLog userLoginLog);

    /**
     * 更新登录日志
     * @param userLoginLog 新日志
     * @return int 影响行
     */
    @Update("UPDATE user_login_log SET last_login_time = NOW(), last_login_ip = #{lastLoginIp}, last_login_device = #{lastLoginDevice}" )
    int updateUserLoginLog(UserLoginLog userLoginLog);

    /**
     * 查询用户日志
     * @param userUuid 用户uuid
     * @return UserLoginLog 用户登录日志
     */
    @Select("SELECT * FROM user_login_log WHERE user_uuid = #{userUuid}")
    UserLoginLog selectLoginLog(String userUuid);

    /**
     * 插入更新设备数量
     * @param device 设备类型
     * @param userUuid 设备数量
     * @return 影响行数
     */
    @Insert(" INSERT INTO device_statistics (${device},user_uuid)" +
            "VALUES (1,#{userUuid})" +
            "ON DUPLICATE KEY UPDATE ${device} = ${device} + 1" )
    int insertOrUpdateDevices(@Param("device") String device, @Param("userUuid") String userUuid);


    /**
     *  删除或者更新总下载数
     * @param totalStats 文件更新信息
     * @return 影响行
     */
    int insertOrUpdateAllCount(TotalStats totalStats );

    /**
     * 插入或者更新文件引用数量
     * @param fileStatistics 文件记录对象
     * @return 影响行数
     */
    int insertOrUpdateFileCount( FileStatistics fileStatistics);

    /**
     * 删除文件引用记录
     * @Param folderUuid 文件夹id
     * @return 影响行数
     */
    @Delete("DELETE FROM file_statistics WHERE file_id IN (SELECT id FROM image WHERE folder_uuid = #{folderUuid})")
    int deleteFileCount(String folderUuid);


    /**
     * 查询用户七天的下载上传总数据
     * @param userUuid
     * @return
     */
    @Select("SELECT * FROM total_stats WHERE user_uuid = #{userUuid} AND date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY date DESC")
    List<TotalStats> searchSevenTotalCount(String userUuid);

    /**
     * 查询用户七天的下载上传总数据
     * @param userUuid
     * @return
     */
    @Select("SELECT SUM(total_upload) AS total_upload, SUM(total_download) AS total_download, SUM(total_delete) AS total_delete " +
            "FROM total_stats WHERE user_uuid = #{userUuid}")
    TotalStats searchTotalCount(String userUuid);

    /**
     * 查询文件引用情况按照时间倒序
     * @param userUuid 用户id
     * @return
     */
    @Select("SELECT * FROM file_statistics WHERE user_uuid = #{userUuid} ORDER BY date DESC LIMIT 10")
    List<FileStatistics> searchFileLinkCount(String userUuid);

    /**
     * 查询文件引用情况按照时间倒序
     * @param userUuid 用户id
     * @return
     */
    @Select("SELECT a.id, a.file_id, a.reference_count,a.user_uuid,a.date,b.image_name " +
            "FROM file_statistics AS a LEFT JOIN image AS b ON a.file_id = b.id " +
            "WHERE a.user_uuid = #{userUuid} ORDER BY a.date DESC LIMIT 10")
    List<FileIncludeNameStatistics> searchFileIncludeNameLinkCount(String userUuid);

    /**
     * 查询用户登录设备数
     * @param userUuid 用户id
     * @return 设备登录数量统计
     */
    @Select("SELECT * FROM  device_statistics WHERE user_uuid = #{userUuid}")
    DeviceStatistics searchDevicesCount(String userUuid);


}
