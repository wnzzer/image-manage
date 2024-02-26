package com.chen.behindimagesmanage.config;

import com.chen.behindimagesmanage.Interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 15031
 */
@Configuration

public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public AccessLimitInterceptor createAccessLimitInterceptor(){
        return new AccessLimitInterceptor();
    }

    @Bean
    public TokenValidationInterceptor createTokenValidationInterceptor(){return new TokenValidationInterceptor();}
    @Bean
    public PicGoAccessInterceptor createPicGoAccessInterceptor(){return new PicGoAccessInterceptor();};

    @Bean
    public SyTokenInterceptor createSyTokenInterceptor(){return new SyTokenInterceptor();
    };

    @Bean
    public AdminTokenInterceptor createAdminTokenInterceptor(){return new AdminTokenInterceptor();
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createAccessLimitInterceptor())
                // 全部接口
                .addPathPatterns("/**")
                        .order(0);
                // 去除某些不需要限制的接口
//                .excludePathPatterns("/sys/test");
        registry.addInterceptor(new NotInitInterceptor())
                .addPathPatterns("/**")
                        .order(1);

        registry.addInterceptor(createTokenValidationInterceptor())
                // 全部接口
                .addPathPatterns("/api/user/**")
                .addPathPatterns("/api/admin/**")
                .order(2);
        registry.addInterceptor(createPicGoAccessInterceptor())
                // 全部接口
                .addPathPatterns("/api/picgo/**")
                .order(2);
        registry.addInterceptor(createSyTokenInterceptor())
                // 全部接口
                .addPathPatterns("/k8s/**")
                .order(2);
        registry.addInterceptor(createAdminTokenInterceptor())
                .addPathPatterns("/api/admin/**")
                .order(2);
        // 去除某些不需要限制的接口
//                .excludePathPatterns("/sys/test");

    }

}

