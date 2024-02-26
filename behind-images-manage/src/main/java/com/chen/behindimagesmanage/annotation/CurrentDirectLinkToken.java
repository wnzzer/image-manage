package com.chen.behindimagesmanage.annotation;


import com.chen.behindimagesmanage.pojo.DirectLinkToken;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 15031
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentDirectLinkToken {
}
