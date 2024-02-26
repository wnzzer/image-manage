package com.chen.behindimagesmanage.filter;


import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * @author 15031
 */
//@Order(0)
//@WebFilter(urlPatterns = "/*", filterName = "Filter")
public class RequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器，可以在这里进行一些配置
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("输出");
        // 在请求处理之前拦截，打印请求体内容
        String requestBody = getRequestBody(request);
        System.out.println("Request Body: " + requestBody);

        // 继续处理请求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁过滤器，可以在这里进行一些清理工作
    }

    // 辅助方法，用于获取请求体内容
    private String getRequestBody(ServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buffer = new char[1024];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            requestBody.append(buffer, 0, bytesRead);
        }
        return requestBody.toString();
    }
}
