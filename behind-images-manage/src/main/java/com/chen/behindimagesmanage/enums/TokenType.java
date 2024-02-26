package com.chen.behindimagesmanage.enums;

import lombok.Getter;

/**
 * @author 15031
 */

@Getter
public enum TokenType {
    K8STOKEN("k8sToken"),
    USERTOKEN("userToken"),
    PICGOTOKEN("picgoToken");

    private final String tokenType;

    TokenType(String tokenType) {
        this.tokenType = tokenType;
    }

}
