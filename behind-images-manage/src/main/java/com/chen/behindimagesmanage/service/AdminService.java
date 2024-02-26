package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.pojo.SimplyUser;
import com.chen.behindimagesmanage.pojo.User;
import com.chen.behindimagesmanage.util.ApiResponse;

import java.util.List;

/**
 * @author 15031
 */
public interface AdminService {
    ApiResponse<List<SimplyUser>> getUsers();
    ApiResponse<String>addUser(User user,String userUuid);

    ApiResponse<String>deleteUser(Integer id,String userUuid);

    ApiResponse<String>putUser(User user,String userUuid);
}
