package com.kakaobean.core.common;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{

    private Integer status;
    private String errorCode;

    public ApplicationException(String message, Integer status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    /**
     * https://dkswnkk.tistory.com/692
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
