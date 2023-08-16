package com.kakaobean.core.issue.exception;

import com.kakaobean.core.common.ApplicationException;

public abstract class CommentException extends ApplicationException {
    public CommentException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}