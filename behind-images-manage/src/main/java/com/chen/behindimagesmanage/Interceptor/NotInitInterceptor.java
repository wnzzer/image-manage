package com.chen.behindimagesmanage.Interceptor;

import com.chen.behindimagesmanage.config.PodDataSynConfig;
import com.chen.behindimagesmanage.util.PodUtil;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
/**
 * @author 15031
 */
@Order(1)
public class NotInitInterceptor implements HandlerInterceptor {
    @Resource
    private PodUtil podUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这是在请求到达控制器之前执行的地方

        // 在这里添加你的逻辑来检查初始化是否完成
        boolean initializationComplete= PodDataSynConfig.initFlag;

        if (initializationComplete) {
            // 如果初始化完成，返回true，请求会继续到达控制器
            return true;
        } else {
            // 如果初始化未完成，将请求转发到其他服务器
            String otherPodUrl = buildRedirectUrl(request,podUtil.getBestPod());
            response.sendRedirect(otherPodUrl);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 这是在控制器执行后，视图渲染前执行的地方
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 这是在视图渲染完成后执行的地方
    }

    private boolean checkInitializationStatus() {
        // 添加你的初始化检查逻辑，返回true表示初始化完成，否则返回false
        // 你可以根据具体需求来实现这个方法
        return true;
        // 这里示例始终返回true，你需要根据实际情况修改
    }
    private String buildRedirectUrl(HttpServletRequest request, String pod) {
        StringBuilder urlBuilder = new StringBuilder("https");
        urlBuilder.append("?");

        Map<String, String[]> queryParams = request.getParameterMap();
        // 重建查询参数部分
        queryParams.forEach((param, values) -> {
            for (String value : values) {
                urlBuilder.append(param).append("=").append(value).append("&");
            }
        });

        // 去除末尾的"&"
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        return urlBuilder.toString();
    }
}

