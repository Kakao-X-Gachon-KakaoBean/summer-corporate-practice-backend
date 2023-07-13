package com.kakaobean.core.releasenote.exception;

public class ManuscriptWriterAccessException extends ReleaseNoteException {

    private static final String message = "릴리즈 노트 원고 작성 권한이 없습니다.";
    private static final String errorCode = "M001";
    private static final Integer status = 400;

    public ManuscriptWriterAccessException() {
        super(message, status, errorCode);
    }
}
