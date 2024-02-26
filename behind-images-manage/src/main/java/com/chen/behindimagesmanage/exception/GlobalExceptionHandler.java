package com.chen.behindimagesmanage.exception;

import com.chen.behindimagesmanage.util.ApiResponse;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 自定义异常处理
 * @author: DT
 * @date: 2021/4/19 21:51
 * @version: v1.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ApiResponse<String> bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ApiResponse.error(Integer.parseInt(e.getErrorCode()),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ApiResponse<String> exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }
    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =AccessOverException.class)
    @ResponseBody
    public ApiResponse<String> accessExceptionHandler(HttpServletRequest req, Exception e){
        log.error("接口进入异常："+e.getMessage());
        return ApiResponse.error(ExceptionEnum.OVER_ACCESS);
    }
    /**
     * 参数异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =ParameterException.class)
    @ResponseBody
    public ApiResponse<String> parameterExceptionHandler(HttpServletRequest req, Exception e){
        log.error("请求参数异常:"+e.getMessage());
        return ApiResponse.error(ExceptionEnum.Unprocessable_Entity);
    }


    @ExceptionHandler(value = NotFindSourceException.class)
    @ResponseBody
    public ApiResponse<String> notFindExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未找到资源异常:"+ e.getMessage());
        return ApiResponse.error(ExceptionEnum.NOT_FOUND);
    }




    @ExceptionHandler(value =ApiException.class)
    @ResponseBody
    public ApiResponse<String> k8sApiHandler(Exception e){
        log.error("k8s api异常！请检查k8sapi服务器状态或者配置文件是否正常，原因是:"+e.getMessage());
        return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ApiResponse<String> exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:"+e.getMessage());
        return ApiResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }



}
