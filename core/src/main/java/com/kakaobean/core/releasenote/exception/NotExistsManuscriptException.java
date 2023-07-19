package com.kakaobean.core.releasenote.exception;

public class NotExistsManuscriptException extends ReleaseNoteException {

    private static final String message = "존재하지 않는 릴리즈 노트 원고입니다.";
    private static final String errorCode = "M002";
    private static final Integer status = 400;

    public NotExistsManuscriptException() {
        super(message, status, errorCode);
    }
}
