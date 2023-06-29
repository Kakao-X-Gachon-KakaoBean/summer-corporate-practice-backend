package com.kakaobean.common.dto;

import lombok.Getter;

@Getter
public class CommandSuccessResponse {

    private static final String SUCCESS_MESSAGE = "요청을 성공하셨습니다.";

    private final String message;

    public CommandSuccessResponse() {
        this.message = SUCCESS_MESSAGE;
    }
}
