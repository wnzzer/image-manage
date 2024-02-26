package com.chen.behindimagesmanage.annotation;

import com.chen.behindimagesmanage.pojo.DirectLinkToken;
import com.chen.behindimagesmanage.util.HttpUtil;
import com.chen.behindimagesmanage.util.TokenUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * @author 15031
 */
@Component
public class PicGoUserHandlerMethodArgumentResolver  implements HandlerMethodArgumentResolver {
    @Resource
    private TokenUtil tokenUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentDirectLinkToken.class) &&
                parameter.getParameterType().isAssignableFrom(DirectLinkToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {
        // header中获取用户token
        String token = request.getHeader("Authorization");
        if(token == null){
            token = request.getParameter("Authorization");
        }
        return tokenUtil.getPicGoToken(token);
    }
}