package com.chen.behindimagesmanage.Interceptor;

import com.chen.behindimagesmanage.util.ApiResponse;
import com.chen.behindimagesmanage.util.HttpUtil;
import com.chen.behindimagesmanage.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 15031
 */
@Slf4j
public class PicGoAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private  TokenUtil tokenUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String receiveToken = HttpUtil.getHeaderOrParameter(request, "Authorization");
        if (tokenUtil.checkPicGoToken(receiveToken)) {
            log.info("picGoToken验证通过");
            return true;
            // Token验证通过，继续请求
        } else {
            log.info(receiveToken + "picGoToken验证不通过");
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(ApiResponse.error(401, "Token is invalid")));
            return false;
            // Token验证失败，终止请求
        }
    }
}
