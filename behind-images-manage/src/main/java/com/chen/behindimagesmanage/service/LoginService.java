package com.chen.behindimagesmanage.service;

import com.chen.behindimagesmanage.dao.LoginDao;
import com.chen.behindimagesmanage.exception.ExceptionEnum;
import com.chen.behindimagesmanage.pojo.UserInformation;

import com.chen.behindimagesmanage.pojo.User;
import com.chen.behindimagesmanage.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author 15031
 */
@Slf4j
@Service
public class LoginService {
    @Resource
    private RedisService redisService;
    @Resource
    private LoginDao loginDao;
    @Resource
    private CountService countService;
    public ApiResponse<UserInformation> checkPass(User user, HttpServletRequest httpServletRequest){
        User fullUser = loginDao.isExist(user);
        System.out.println(fullUser);
        if (fullUser != null){
            String token = "Bearer "+UUID.randomUUID();
            redisService.setValue(token,fullUser.getUuid(),30);
            UserInformation currentUserInformation = new UserInformation(fullUser,token);
            log.info("当前登录用户" + currentUserInformation);
            countService.setLastLoginPosition(httpServletRequest,fullUser.getUuid());
            return ApiResponse.success(currentUserInformation);

        }else {
            return ApiResponse.error(ExceptionEnum.Unprocessable_Entity,"账号或者密码错误");

        }

    }

    public ApiResponse<String> register(User user){
       User isUser = loginDao.isExist(user);
       if(isUser == null){
           String uuid = UUID.randomUUID().toString();
           User newUser = new User(user.getUsername(), user.getPassword(),uuid,1 );
           int count = loginDao.insertUser(user);
           if(count >= 1){
               return ApiResponse.success("成功");
           }else {
               return ApiResponse.error(422,"账号插入失败");
           }
       }else{
           return ApiResponse.error(400,"账号已经存在");

       }

    }
}
