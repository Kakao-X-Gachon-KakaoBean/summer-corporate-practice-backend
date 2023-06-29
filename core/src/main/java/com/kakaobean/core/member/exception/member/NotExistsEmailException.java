package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class NotExistsEmailException extends MemberException{

    private static final String message = "존재하지 않는 유저의 이메일입니다.";
    private static final String errorCode = "M002";
    private static final Integer status = 400;

    public NotExistsEmailException() {
        super(message, status, errorCode);
    }
}