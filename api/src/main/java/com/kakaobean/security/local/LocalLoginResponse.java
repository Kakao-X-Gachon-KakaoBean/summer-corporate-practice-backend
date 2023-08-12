package com.kakaobean.security.local;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class LocalLoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String tokenType = "Bearer";

    public LocalLoginResponse(String accessToken, String refreshToken, Long memberId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}
