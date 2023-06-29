package com.kakaobean.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationExceptionResponse {

    private String message;
    private String errorCode;
    private Integer status;

    public ApplicationExceptionResponse(String message, String errorCode, Integer status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
    }
}
