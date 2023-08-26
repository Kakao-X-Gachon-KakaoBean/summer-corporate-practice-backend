package com.kakaobean.core.releasenote.exception;

public class CannotDeleteManuscriptException extends ReleaseNoteException {

    private static final String message = "는 릴리즈 노트 원고를 삭제할 수 없습니다.";
    private static final String errorCode = "Ma006";
    private static final Integer status = 400;

    public CannotDeleteManuscriptException(String role) {
        super(role + message, status, errorCode);
    }
}

