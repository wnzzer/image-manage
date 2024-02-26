package com.chen.behindimagesmanage.dao;

import com.chen.behindimagesmanage.pojo.SimplyUser;
import com.chen.behindimagesmanage.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 15031
 */
@Mapper
public interface AdminDao {
    @Select("SELECT a.id,a.uuid,a.username,a.thumbnail_avatar,a.level,b.last_login_time FROM user AS  a LEFT JOIN user_login_log AS b " +
            "ON  a.uuid = b.user_uuid")
    List<SimplyUser>selectUsers();

    @Select("SELECT * FROM  user WHERE  uuid = #{userUuid}")
    User selectUserByUuid(String userUuid);

    @Select("SELECT * FROM  user WHERE  username = #{userName}")
    User selectUserByUsername(String username);

    @Select("SELECT * FROM  user WHERE  id = #{id}")
    User selectUserByUserId(int id);
    @Insert("INSERT INTO user (uuid,username,password,level) VALUES (#{uuid},#{username},#{password},#{level})")
    int insertUser(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUserById (int id);

    int putUser(User user);


}
