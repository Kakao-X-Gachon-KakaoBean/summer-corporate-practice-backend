package com.kakaobean.security;

import com.kakaobean.core.common.ApplicationException;
import lombok.Getter;

@Getter
public abstract class AuthException extends ApplicationException {

    public AuthException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}
