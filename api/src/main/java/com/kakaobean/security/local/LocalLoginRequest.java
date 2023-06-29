package com.kakaobean.security.local;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LocalLoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public LocalLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
