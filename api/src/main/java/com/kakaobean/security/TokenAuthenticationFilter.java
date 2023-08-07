package com.kakaobean.security;

import com.kakaobean.security.token.RefreshTokenRepository;
import com.kakaobean.security.token.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String REFRESH_TOKEN_HEADER = "refresh-token";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("access token 검증 시작");
            String accessToken = getAccessTokenFromRequest(request);
            validToken(request, accessToken);
            log.info("access token 검증 종료");
        } catch (ExpiredJwtException ex) {
            log.info("refresh token 검증 시작");
            String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
            Authentication authentication = validToken(request, refreshToken);
            if(authentication != null){

                if(refreshTokenRepository.findByAuthentication(authentication).isEmpty()){
                    throw new RuntimeException("발급하지 않은 리프레쉬 토큰입니다.");
                }

                refreshTokenRepository.deleteByAuthentication(authentication);
                refreshTokenRepository.save(authentication, refreshToken);

                response.setHeader(AUTHORIZATION_HEADER, BEARER_TOKEN + tokenProvider.createAccessToken(authentication));
                response.setHeader(REFRESH_TOKEN_HEADER, refreshToken);
                log.info("refresh token 검증 성공 후 종료");
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private Authentication validToken(HttpServletRequest request, String accessToken) {
        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            Long userId = tokenProvider.getUserIdFromToken(accessToken);

            UserPrincipal userDetails = (UserPrincipal) customUserDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, //여기에 어떤 값을 principal 넣는지가 중요함.
                    null,
                    userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        }
        return null;
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TOKEN)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
