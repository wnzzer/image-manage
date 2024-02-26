package com.chen.behindimagesmanage.dao;

import com.chen.behindimagesmanage.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * @author 15031
 */
@Mapper
public interface LoginDao {
    /**
     * 验证账号在数据库存在
     * @param user 用户信息（账号密码）
     * @return int 查询值数量
     */
    @Select("SELECT * FROM user WHERE username = #{user.username} AND password = #{user.password}")
    User isExist(@Param("user") User user);

    /**
     * 插入账号
     * @param user 用户信息（账号密码）
     * @return int 查询值数量
     */
    @Insert("INSERT INTO user (username, password, uuid, level) VALUES (#{user.username}, #{user.password}, #{user.uuid}, #{user.level})")
    int insertUser(@Param("user") User user);
//    void insertUser(@Param("user") User user);
//
//    @Update("UPDATE user SET password = #{user.password}, uuid = #{user.uuid}, level = #{user.level} WHERE id = #{user.id}")
//    void updateUser(@Param("user") User user);
//
//    @Delete("DELETE FROM user WHERE id = #{id}")
//    void deleteUser(@Param("id") Long id);
}
