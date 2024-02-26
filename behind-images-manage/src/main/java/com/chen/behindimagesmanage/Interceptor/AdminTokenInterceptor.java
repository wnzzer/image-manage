package com.chen.behindimagesmanage.Interceptor;

import com.chen.behindimagesmanage.dao.AdminDao;
import com.chen.behindimagesmanage.pojo.User;
import com.chen.behindimagesmanage.service.RedisService;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.HttpUtil;
import com.chen.behindimagesmanage.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class AdminTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;
    @Resource
    AdminDao adminDao;

    @Resource
    TokenUtil tokenUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在这里进行Token验证的逻辑
        // 检查请求中的Token是否有效，是否包含正确的权限等
        // 如果Token验证通过，继续请求链的执行
        // 如果Token验证失败，可以返回适当的错误响应或进行重定向

        String token = HttpUtil.getHeaderOrParameter(request,"Authorization");
        String userUuid = tokenUtil.getUserToken(token);
        User user = adminDao.selectUserByUuid(userUuid);
        if(user.getLevel() >= 8){
            return true;
        }else {
            response.getWriter().write(new ObjectMapper().writeValueAsString(ApiResponse.error(401, "Token is invalid")));
            return false;

        }

    }

    private boolean isValidToken(String token) {
        // 在这里实现Token验证的逻辑
        // 可以检查Token是否有效、是否过期、是否包含正确的权限等
        // 返回true表示Token验证通过，返回false表示验证失败
        log.info(token);
        return redisService.isValidToken(token);
    }
}
