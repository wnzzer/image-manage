package com.chen.behindimagesmanage.config;

import com.chen.behindimagesmanage.annotation.LoginUserHandlerMethodArgumentResolver;
import com.chen.behindimagesmanage.annotation.PicGoUserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 15031
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;
    @Resource
    private PicGoUserHandlerMethodArgumentResolver picGoUserHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
        argumentResolvers.add(picGoUserHandlerMethodArgumentResolver);
    }


}