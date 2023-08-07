package com.kakaobean.security.exception;

public class NotExistsRefreshTokenException extends AuthException {

    private static final String message = "존재하지 않는 리프레쉬 토큰입니다.";

    public NotExistsRefreshTokenException() {
        super(message);
    }
}

