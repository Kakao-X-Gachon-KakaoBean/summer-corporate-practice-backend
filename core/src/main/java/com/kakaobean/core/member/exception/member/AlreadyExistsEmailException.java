package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class AlreadyExistsEmailException extends MemberException{

    private static final String message = "이미 존재하는 유저의 이메일입니다.";
    private static final String errorCode = "M003";
    private static final Integer status = 400;

    public AlreadyExistsEmailException() {
        super(message, status, errorCode);
    }
}