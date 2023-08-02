package com.kakaobean.core.sprint.Exception;

public class SprintAccessException extends SprintException{

    private static final String message = "스프린트에 생성 및 변경에 대한 권한이 없습니다.";
    private static final String errorCode = "S001";
    private static final Integer status = 400;

    public SprintAccessException() {
        super(message, status, errorCode);
    }
}
