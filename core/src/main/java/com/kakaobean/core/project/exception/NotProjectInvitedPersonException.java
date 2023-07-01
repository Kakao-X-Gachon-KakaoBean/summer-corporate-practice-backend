package com.kakaobean.core.project.exception;

public class NotProjectInvitedPersonException extends ProjectException {

    private static final String message = "프로젝트에 초대된 회원이 아닙니다.";
    private static final String errorCode = "P004";
    private static final Integer status = 400;

    public NotProjectInvitedPersonException() {
        super(message, status, errorCode);
    }
}
