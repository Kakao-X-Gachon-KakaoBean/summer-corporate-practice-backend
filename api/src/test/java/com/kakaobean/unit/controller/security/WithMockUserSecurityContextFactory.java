package com.kakaobean.unit.controller.security;

import com.kakaobean.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static java.util.Collections.singleton;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserPrincipal(1L , "hello@gmail.com", "pwd", null),
                customUser.password(),
                singleton(authority)
        );

        context.setAuthentication(token);
        return context;
    }
}
