package com.kakaobean.core.releasenote.exception;

public class ManuscriptModificationAccessException extends ReleaseNoteException {

    private static final String message = "릴리즈 노트 원고 수정 권한이 없습니다.";
    private static final String errorCode = "M004";
    private static final Integer status = 400;

    public ManuscriptModificationAccessException() {
        super(message, status, errorCode);
    }
}
