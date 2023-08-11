package com.kakaobean.core.sprint.exception;

import com.kakaobean.core.common.ApplicationException;
import lombok.Getter;

@Getter
public abstract class SprintException extends ApplicationException {

    public SprintException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}
