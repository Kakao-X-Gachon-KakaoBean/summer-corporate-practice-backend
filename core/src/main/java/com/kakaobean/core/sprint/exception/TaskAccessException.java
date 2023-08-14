package com.kakaobean.core.sprint.exception;

public class TaskAccessException extends SprintException {

    private static final String message = "해당 작업에 대한 권한이 없습니다.";
    private static final String errorCode = "T001";
    private static final Integer status = 400;

    public TaskAccessException() {
        super(message, status, errorCode);
    }
}
