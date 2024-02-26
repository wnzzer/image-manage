package com.chen.behindimagesmanage.exception;

/**
 * @description: 异常处理枚举类
 * @author: DT
 * @date: 2021/4/19 21:41
 * @version: v1.0
 */
public enum ExceptionEnum implements BaseErrorInfoInterface{

    // 数据操作错误定义

    SUCCESS("200", "成功!"),
    OVER_ACCESS("429","访问过快"),
    BODY_NOT_MATCH("400","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("401","请求的数字签名不匹配!"),
    TOKEN_ERROR("401","token不存在"),
    NOT_FOUND("404", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
    SERVER_BUSY("503","服务器正忙，请稍后再试!"),
    Unprocessable_Entity("422","参数错误"),

    K8SAPI_NOT_CONNECT("500","k8s api无法访问");


    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
