package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class NotExistsMembersInfoException extends MemberException{

    private static final String message = "멤버 정보가 없습니다.";
    private static final String errorCode = "M004";
    private static final Integer status = 400;

    public NotExistsMembersInfoException() {
        super(message, status, errorCode);
    }
}
