package com.kakaobean.core.member.application.dto.response;

import lombok.Getter;

@Getter
public class RegisterMemberResponseDto {

    private final Long memberId;

    public RegisterMemberResponseDto(Long memberId) {
        this.memberId = memberId;
    }
}
