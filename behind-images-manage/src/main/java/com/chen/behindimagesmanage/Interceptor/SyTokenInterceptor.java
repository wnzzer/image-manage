package com.chen.behindimagesmanage.Interceptor;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 15031
 */
@Slf4j
public class SyTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String syKey = request.getHeader("sy_key");
        if (syKey == null) {
            syKey = request.getParameter("sy_key");
        }
        if (PodDataSynConfig.SY_KEY_VALUE.equals(syKey)) {
            log.info("syToken验证通过");
            return true;
            // Token验证通过，继续请求
        } else {
            log.info(syKey + "syToken验证不通过");
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(ApiResponse.error(401, "Token is invalid")));
            return false;
            // Token验证失败，终止请求
        }
    }
}

