package com.kakaobean.core.sprint.exception;

public class NotExistsTaskException extends SprintException {

    private static final String message = "존재하지 않는 스프린트 입니다.";
    private static final String errorCode = "T002";
    private static final Integer status = 400;

    public NotExistsTaskException() {
        super(message, status, errorCode);
    }
}
