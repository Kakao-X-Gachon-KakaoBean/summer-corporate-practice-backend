package com.kakaobean.core.sprint.exception;

public class AssignmentNotAllowedException extends SprintException {

    private static final String message = "Viewer 에게는 작업을 할당할 수 없습니다.";
    private static final String errorCode = "T003";
    private static final Integer status = 400;

    public AssignmentNotAllowedException() {
        super(message, status, errorCode);
    }
}
