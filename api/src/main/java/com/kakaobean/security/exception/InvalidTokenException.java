package com.kakaobean.security.exception;

public class InvalidTokenException extends AuthException {

    private static final String message = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(message);
    }
}

