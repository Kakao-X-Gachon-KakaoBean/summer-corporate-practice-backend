package com.kakaobean.core.project.exception;

public class NotProjectAdminException extends ProjectException {

    private static final String message = "역할은 동작을 진행할 수 없습니다. 프로젝트 관리자만 해당 동작을 진행할 수 있습니다.";
    private static final String errorCode = "P003";
    private static final Integer status = 400;

    public NotProjectAdminException(String role) {
        super(role + message, status, errorCode);
    }
}
