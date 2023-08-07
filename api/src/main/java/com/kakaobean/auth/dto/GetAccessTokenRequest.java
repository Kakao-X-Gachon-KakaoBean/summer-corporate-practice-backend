package com.kakaobean.auth.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAccessTokenRequest {

    private String refreshToken;

    public GetAccessTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
