package com.kakaobean.core.member.application.dto.response;

import lombok.Getter;

@Getter
public class FindEmailResponseDto {

    private String email;

    public FindEmailResponseDto(String email) {
        this.email = email;
    }
}
