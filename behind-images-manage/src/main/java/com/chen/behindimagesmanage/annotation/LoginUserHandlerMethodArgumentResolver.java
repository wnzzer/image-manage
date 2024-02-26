package com.chen.behindimagesmanage.annotation;

import com.chen.behindimagesmanage.util.TokenUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 * @author 15031
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private TokenUtil tokenUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserUuid.class) &&
                parameter.getParameterType().isAssignableFrom(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {
        // header中获取用户token
        String token = request.getHeader("Authorization");
        if(token == null){
            token = request.getParameter("Authorization");
        }
        // TODO 根据userId获取User信息，这里省略，直接创建一个User对象。
        return tokenUtil.getUserToken(token);
    }
}