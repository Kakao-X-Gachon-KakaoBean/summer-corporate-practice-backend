package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class ChangingNameToSameNameException extends MemberException{

    private static final String message = "바꾸려는 이름과 현재 이름이 동일합니다.";
    private static final String errorCode = "M007";
    private static final Integer status = 400;

    public ChangingNameToSameNameException() {
        super(message, status, errorCode);
    }
}