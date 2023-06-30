package com.kakaobean.core.project.exception;

import com.kakaobean.core.common.ApplicationException;

public abstract class ProjectException extends ApplicationException {
    public ProjectException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}
