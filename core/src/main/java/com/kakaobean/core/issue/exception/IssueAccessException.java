package com.kakaobean.core.issue.exception;


public class IssueAccessException extends IssueException {

    private static final String message = "이슈 생성 및 변경에 대한 권한이 없습니다.";
    private static final String errorCode = "I002";
    private static final Integer status = 400;

    public IssueAccessException() {
        super(message, status, errorCode);
    }
}