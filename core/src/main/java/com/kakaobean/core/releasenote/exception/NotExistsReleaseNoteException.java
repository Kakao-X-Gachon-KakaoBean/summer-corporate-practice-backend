package com.kakaobean.core.releasenote.exception;

public class NotExistsReleaseNoteException extends ReleaseNoteException {

    private static final String message = "존재하지 않는 릴리즈 노트입니다.";
    private static final String errorCode = "R001";
    private static final Integer status = 400;

    public NotExistsReleaseNoteException() {
        super(message, status, errorCode);
    }
}
