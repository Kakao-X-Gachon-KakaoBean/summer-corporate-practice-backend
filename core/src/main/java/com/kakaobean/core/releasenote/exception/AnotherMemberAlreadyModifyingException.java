package com.kakaobean.core.releasenote.exception;

public class AnotherMemberAlreadyModifyingException extends ReleaseNoteException {

    private static final String message = "릴리즈 노트 원고를 이미 다른 멤버가 수정하고 있습니다.";
    private static final String errorCode = "Ma007";
    private static final Integer status = 400;

    public AnotherMemberAlreadyModifyingException() {
        super(message, status, errorCode);
    }
}
