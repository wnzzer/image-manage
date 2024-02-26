package com.chen.behindimagesmanage.util;

import com.chen.behindimagesmanage.exception.BaseErrorInfoInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 15031
 */
@Slf4j
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public ApiResponse() {
        // 默认构造函数
    }

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<String> success() {
        return new ApiResponse<>(200, "Success","success");
    }
    public static <T> ApiResponse<T> success(T data) {
        log.info("成功返回:"+(data.toString().length() < 300 ? data.toString() : data.toString().substring(0,300)));
        return new ApiResponse<>(200, "Success", data);
    }
    public static <T> ApiResponse<T> success(String message,T data) {
        log.info("成功返回:"+(data.toString().length() < 300 ? data.toString() : data.toString().substring(0,300)));
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {
        log.info(statusCode+ ":" + message);
        return new ApiResponse<>(statusCode, message, null);
    }
    public static <T> ApiResponse<T> error(BaseErrorInfoInterface errorInfo) {
        log.info(errorInfo.getResultCode() + ":" + errorInfo.getResultMsg());
        return new ApiResponse<>(Integer.parseInt(errorInfo.getResultCode()), errorInfo.getResultMsg(), null);
    }

    public static <T> ApiResponse<T> error(BaseErrorInfoInterface errorInfo,String message) {
        log.info(errorInfo.getResultCode() + ":" + errorInfo.getResultMsg());
        return new ApiResponse<>(Integer.parseInt(errorInfo.getResultCode()), message, null);
    }
}
