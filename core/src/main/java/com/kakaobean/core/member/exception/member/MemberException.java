package com.kakaobean.core.member.exception.member;

import com.kakaobean.core.common.ApplicationException;
import lombok.Getter;

@Getter
public abstract class MemberException extends ApplicationException {

    public MemberException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}
