package com.kakaobean.security.token;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(Authentication authentication, String refreshToken);
    Optional<String> findByAuthentication(Authentication authentication);
    void deleteByAuthentication(Authentication authentication);
}
