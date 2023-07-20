package com.kakaobean.core.releasenote.exception;

public class DuplicateManuscriptVersionException extends ReleaseNoteException {

    private static final String message = "중복된 릴리즈 노트 원고 버전이 있습니다.";
    private static final String errorCode = "M004";
    private static final Integer status = 400;

    public DuplicateManuscriptVersionException() {
        super(message, status, errorCode);
    }
}
