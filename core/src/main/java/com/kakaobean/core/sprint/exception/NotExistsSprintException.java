package com.kakaobean.core.sprint.exception;

public class NotExistsSprintException extends SprintException{

    private static final String message = "존재하지 않는 스프린트 입니다.";
    private static final String errorCode = "S003";
    private static final Integer status = 400;

    public NotExistsSprintException() {
        super(message, status, errorCode);
    }
}
