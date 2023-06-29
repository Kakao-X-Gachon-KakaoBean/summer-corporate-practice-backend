package com.kakaobean.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendVerifiedEmailRequest {

    private String email;

    public SendVerifiedEmailRequest(String email) {
        this.email = email;
    }
}
