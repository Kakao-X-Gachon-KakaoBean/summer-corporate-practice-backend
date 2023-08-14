package com.kakaobean.core.issue.exception;

public class CommentAccessException extends CommentException{

    private static final String message = "댓글 생성 및 변경에 대한 권한이 없습니다.";
    private static final String errorCode = "C002";
    private static final Integer status = 400;

    public CommentAccessException() {
        super(message, status, errorCode);
    }

}
