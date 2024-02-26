package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.dao.AdminDao;
import com.chen.behindimagesmanage.dao.UserDao;
import com.chen.behindimagesmanage.exception.ExceptionEnum;
import com.chen.behindimagesmanage.pojo.SimplyUser;
import com.chen.behindimagesmanage.pojo.User;
import com.chen.behindimagesmanage.util.ApiResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author 15031
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminDao adminDao;
    @Resource
    private UserDao userDao;
    @Override
    public ApiResponse<List<SimplyUser>> getUsers() {
        return ApiResponse.success(adminDao.selectUsers());
    }

    @Override
    public ApiResponse<String> addUser(User user,String userUuid) {
        User currentUser = adminDao.selectUserByUuid(userUuid);
        System.out.println(currentUser);
        if(currentUser.getLevel() > user.getLevel()){
            System.out.println(user.getLevel());
            User isUsed =  adminDao.selectUserByUsername(user.getUsername());
            if(isUsed == null){
                user.setUuid(UUID.randomUUID().toString());
                int count = adminDao.insertUser(user);
                if(count >= 1){
                    return  ApiResponse.success("添加成功");
                }
            }else {
                return ApiResponse.error(422,"用户名已经存在");

            }

        }else {
            return  ApiResponse.error(ExceptionEnum.Unprocessable_Entity);
        }
        return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);


    }

    @Override
    public ApiResponse<String> deleteUser(Integer id,String userUuid) {
        User currentUser = adminDao.selectUserByUuid(userUuid);
        User isDeleteUser = adminDao.selectUserByUserId(id);
        if(currentUser.getLevel() > isDeleteUser.getLevel()){
            int count = adminDao.deleteUserById(id);
            if(count >= 1){
                return  ApiResponse.success("删除成功");
            }
        }else {
            return ApiResponse.error(ExceptionEnum.Unprocessable_Entity);
        }
        return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ApiResponse<String> putUser(User user, String userUuid) {
        User currentUser = adminDao.selectUserByUuid(userUuid);
        User isPutUser = adminDao.selectUserByUserId(user.getId());
        if(currentUser.getLevel() <= isPutUser.getLevel() || (user.getLevel() != 0 && user.getLevel() >= currentUser.getLevel())){
            return ApiResponse.error(422,"你只能操作比能权限低低用户");
        }
        int count = adminDao.putUser(user);
        if(count >= 1){
            return ApiResponse.success("修改成功");
        }else {
            return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }


    }
}
