package com.chen.behindimagesmanage.exception;

/**
 * @author 15031
 */
public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String message) {

            super(message);

    }
}
