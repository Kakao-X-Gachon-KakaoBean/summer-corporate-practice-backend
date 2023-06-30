package com.kakaobean.core.project.exception;

public class NotExistsProjectException extends ProjectException {

    private static final String message = "존재하지 않는 프로젝트입니다.";
    private static final String errorCode = "P001";
    private static final Integer status = 400;

    public NotExistsProjectException() {
        super(message, status, errorCode);
    }
}
