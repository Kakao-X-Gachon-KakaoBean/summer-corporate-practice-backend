package com.kakaobean.core.project.exception;

public class NotExistsProjectMemberException extends ProjectException {

    private static final String message = "존재하지 않는 프로젝트 멤버입니다.";
    private static final String errorCode = "P002";
    private static final Integer status = 400;

    public NotExistsProjectMemberException() {
        super(message, status, errorCode);
    }
}
