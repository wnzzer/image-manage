package com.chen.behindimagesmanage.Interceptor;

import com.chen.behindimagesmanage.service.RedisService;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 15031
 */
@Slf4j
public class TokenValidationInterceptor implements HandlerInterceptor {
    @Autowired
    private  RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在这里进行Token验证的逻辑
        // 检查请求中的Token是否有效，是否包含正确的权限等
        // 如果Token验证通过，继续请求链的执行
        // 如果Token验证失败，可以返回适当的错误响应或进行重定向

        String token = request.getHeader("Authorization");
        if (token == null) {
            token = request.getParameter("Authorization");
        }
        if (token != null && isValidToken(token)) {
            log.info("Token验证通过");
            return true;
            // Token验证通过，继续请求
        } else {
            log.info(token + "Token验证不通过");
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(ApiResponse.error(401, "Token is invalid")));
            return false;
            // Token验证失败，终止请求
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
