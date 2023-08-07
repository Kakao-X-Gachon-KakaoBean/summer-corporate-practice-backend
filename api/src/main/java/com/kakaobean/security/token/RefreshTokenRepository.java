package com.kakaobean.security.token;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(String key, String refreshToken);
    Optional<String> findByAuthentication(String key);
    void deleteByAuthentication(String key);
}
