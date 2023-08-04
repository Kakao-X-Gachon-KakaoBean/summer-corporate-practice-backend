package com.kakaobean.core.issue.exception;

import com.kakaobean.core.issue.exception.IssueException;

public class NotExistsIssueException extends IssueException {

    private static final String message = "존재하지 않는 이슈입니다.";
    private static final String errorCode = "I001";
    private static final Integer status = 400;

    public NotExistsIssueException() {
        super(message, status, errorCode);
    }
}

