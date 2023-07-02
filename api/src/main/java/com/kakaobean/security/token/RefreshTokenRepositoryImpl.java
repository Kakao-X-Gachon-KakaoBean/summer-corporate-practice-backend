package com.kakaobean.security.token;


import com.kakaobean.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private final AppProperties appProperties;

    public void save(Authentication authentication, String refreshToken){
        long refreshTokenExpirationMsec = appProperties.getAuth().getRefreshTokenExpirationMsec();
        redisTemplate.opsForValue().set(authentication.getName(), refreshToken, Duration.ofSeconds(refreshTokenExpirationMsec));
    }

    @Override
    public Optional<String> findByAuthentication(Authentication authentication) {
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void deleteByAuthentication(Authentication authentication) {
        redisTemplate.delete(authentication.getName());
    }
}
