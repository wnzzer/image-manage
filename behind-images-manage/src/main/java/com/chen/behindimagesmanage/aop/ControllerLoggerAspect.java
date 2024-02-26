package com.chen.behindimagesmanage.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author 15031
 */
@Slf4j
@Aspect
@Component
public class ControllerLoggerAspect {

    @Before("execution(* com.chen.behindimagesmanage.controller.*.*(..))")
    public void logController(JoinPoint joinPoint) {
        // 记录请求信息，例如请求路径、方法、参数等
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("Controller method " + className + "." + methodName + " is invoked.");
    }
}
