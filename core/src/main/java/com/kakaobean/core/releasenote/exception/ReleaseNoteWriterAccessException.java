package com.kakaobean.core.releasenote.exception;

public class ReleaseNoteWriterAccessException extends ReleaseNoteException {

    private static final String message = "는 릴리즈 노트 배포 권한이 없습니다.";
    private static final String errorCode = "R002";
    private static final Integer status = 400;

    public ReleaseNoteWriterAccessException(String role) {
        super(role + message, status, errorCode);
    }
}
