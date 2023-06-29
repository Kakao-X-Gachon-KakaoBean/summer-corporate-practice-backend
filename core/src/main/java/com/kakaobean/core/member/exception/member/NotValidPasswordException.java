package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class NotValidPasswordException extends MemberException{

    private static final String message = "이메일 인증번호가 틀립니다.";
    private static final String errorCode = "M005";
    private static final Integer status = 400;

    public NotValidPasswordException() {
        super(message, status, errorCode);
    }
}
