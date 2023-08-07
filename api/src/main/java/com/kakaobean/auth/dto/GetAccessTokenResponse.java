package com.kakaobean.auth.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAccessTokenResponse {

    private String accessToken;

    public GetAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
