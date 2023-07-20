package com.kakaobean.core.releasenote.exception;

public class CannotModifyManuscriptException extends ReleaseNoteException {

    private static final String message = "릴리즈 노트 원고를 수정할 수 없습니다.";
    private static final String errorCode = "M005";
    private static final Integer status = 400;

    public CannotModifyManuscriptException() {
        super(message, status, errorCode);
    }
}

