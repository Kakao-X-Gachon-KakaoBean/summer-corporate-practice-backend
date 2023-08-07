package com.kakaobean.core.sprint.Exception;

public class InvalidSprintDateException extends SprintException{

    private static final String message = "설정한 스프린트 마감 날짜가 생성 날짜 이후입니다.";
    private static final String errorCode = "S002";
    private static final Integer status = 400;

    public InvalidSprintDateException() {
        super(message, status, errorCode);
    }
}
