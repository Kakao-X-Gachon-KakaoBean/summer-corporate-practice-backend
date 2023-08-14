package com.kakaobean.core.issue.exception;

public class NotExistsCommentException extends CommentException {

    private static final String message = "존재하지 않는 댓글입니다.";
    private static final String errorCode = "C001";
    private static final Integer status = 400;

    public NotExistsCommentException() {
        super(message, status, errorCode);
    }
}

