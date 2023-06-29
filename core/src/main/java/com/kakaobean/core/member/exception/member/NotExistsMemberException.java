package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class NotExistsMemberException extends MemberException{

    private static final String message = "존재하지 않는 유저입니다.";
    private static final String errorCode = "M001";
    private static final Integer status = 400;

    public NotExistsMemberException() {
        super(message, status, errorCode);
    }
}
