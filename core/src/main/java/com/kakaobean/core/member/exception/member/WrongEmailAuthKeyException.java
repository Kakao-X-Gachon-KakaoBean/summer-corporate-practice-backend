package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class WrongEmailAuthKeyException extends MemberException {

    private static final String message = "이메일 인증번호가 틀립니다.";
    private static final String errorCode = "V008";
    private static final Integer status = 400;

    public WrongEmailAuthKeyException() {
        super(message, status, errorCode);
    }
}
