package com.kakaobean.core.member.exception.member;

import lombok.Getter;

@Getter
public class OAuthMemberCanNotChangePasswordException extends MemberException{

    private static final String message = "로컬 회원가입만 비밀번호를 수정할 수 있습니다.";
    private static final String errorCode = "M006";
    private static final Integer status = 400;

    public OAuthMemberCanNotChangePasswordException() {
        super(message, status, errorCode);
    }
}
