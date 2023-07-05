package com.kakaobean.core.releasenote.exception;

import com.kakaobean.core.common.ApplicationException;
import lombok.Getter;

@Getter
public abstract class ReleaseNoteException extends ApplicationException {

    public ReleaseNoteException(String message, Integer status, String errorCode) {
        super(message, status, errorCode);
    }
}
