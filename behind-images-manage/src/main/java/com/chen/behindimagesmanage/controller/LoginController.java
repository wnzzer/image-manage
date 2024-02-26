package com.chen.behindimagesmanage.controller;

import com.chen.behindimagesmanage.pojo.User;
import com.chen.behindimagesmanage.pojo.UserInformation;
import com.chen.behindimagesmanage.service.CountService;
import com.chen.behindimagesmanage.service.LoginService;
import com.chen.behindimagesmanage.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 15031
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {
    @Resource
    private LoginService loginService;
    @Resource
    private CountService countService;


    @PostMapping("/login")
    public ApiResponse<UserInformation> login(@RequestBody User user, HttpServletRequest httpServletRequest){
        log.info("登录接口被请求，用户："+user);
        return loginService.checkPass(user,httpServletRequest);
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody User user){
       return loginService.register(user);
    }

}
