package com.kakaobean.independentlysystem.email.dto;

import lombok.Getter;

@Getter
public class EmailSenderResponse {
    private final String authKey;
    private final String email;

    public EmailSenderResponse(String authKey, String email) {
        this.authKey = authKey;
        this.email = email;
    }
}
