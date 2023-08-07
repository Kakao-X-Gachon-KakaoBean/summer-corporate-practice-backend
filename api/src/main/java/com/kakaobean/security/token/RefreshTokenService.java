package com.kakaobean.security.token;

import com.kakaobean.security.exception.NotExistsRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String save(Authentication authentication, String refreshToken) {
        String key = authentication.getName();
        if(refreshTokenRepository.findByAuthentication(key).isEmpty()){
            throw new NotExistsRefreshTokenException();
        }
        refreshTokenRepository.deleteByAuthentication(key);
        refreshTokenRepository.save(key, refreshToken);
        return tokenProvider.createAccessToken(authentication);
    }
}
