package com.kakaobean.auth;

import com.kakaobean.auth.dto.GetAccessTokenResponse;
import com.kakaobean.auth.dto.GetAccessTokenRequest;
import com.kakaobean.security.exception.InvalidTokenException;
import com.kakaobean.security.token.RefreshTokenService;
import com.kakaobean.security.token.TokenProvider;
import com.kakaobean.security.token.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final TokenValidator tokenValidator;

    @PostMapping("/access-tokens")
    public ResponseEntity getAccessToken(HttpServletRequest request, @Validated @RequestBody GetAccessTokenRequest body) {
        String refreshToken = body.getRefreshToken();
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException();
        }
        Authentication authentication = tokenValidator.validToken(request, refreshToken);
        String accessToken = refreshTokenService.save(authentication, refreshToken);
        return new ResponseEntity(new GetAccessTokenResponse(accessToken), CREATED);
    }
}
