package com.kakaobean.security.token;

import com.kakaobean.security.UserPrincipal;
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
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String id = String.valueOf(userPrincipal.getId());
        if(refreshTokenRepository.findByAuthentication(id).isEmpty()){
            throw new NotExistsRefreshTokenException();
        }
        refreshTokenRepository.deleteByAuthentication(id);
        refreshTokenRepository.save(id, refreshToken);
        return tokenProvider.createAccessToken(authentication);
    }
}
