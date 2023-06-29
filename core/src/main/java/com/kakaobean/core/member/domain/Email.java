package com.kakaobean.core.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Email {

    private String email;
    private String authKey;

    public Email(String email, String authKey) {
        this.email = email;
        this.authKey = authKey;
    }
}
