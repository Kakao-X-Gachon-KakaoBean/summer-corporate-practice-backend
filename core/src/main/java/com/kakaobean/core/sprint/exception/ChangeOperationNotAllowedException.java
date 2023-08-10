package com.kakaobean.core.sprint.exception;

public class ChangeOperationNotAllowedException extends SprintException{

    private static final String message = "작업상태를 바꿀수있는 권한이 없습니다.";
    private static final String errorCode = "S002";
    private static final Integer status = 400;

    public ChangeOperationNotAllowedException() {
        super(message, status, errorCode);
    }
}
