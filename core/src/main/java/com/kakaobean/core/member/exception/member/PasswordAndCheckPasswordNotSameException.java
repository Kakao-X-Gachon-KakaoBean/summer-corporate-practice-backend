package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class PasswordAndCheckPasswordNotSameException extends MemberException{

    private static final String message = "검증 비밀번호와 비밀번호가 다릅니다.";
    private static final String errorCode = "M006";
    private static final Integer status = 400;

    public PasswordAndCheckPasswordNotSameException() {
        super(message, status, errorCode);
    }
}
