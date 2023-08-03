package com.kakaobean.core.issue.exception;

import com.kakaobean.core.common.ApplicationException;

public abstract class IssueException extends ApplicationException {
    public IssueException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}
