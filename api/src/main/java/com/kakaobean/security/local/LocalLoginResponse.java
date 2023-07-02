package com.kakaobean.security.local;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocalLoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public LocalLoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
