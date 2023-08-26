package com.kakaobean.common;

import com.kakaobean.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@Slf4j
public class GlobalLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(object.getClass() == UserPrincipal.class){
                UserPrincipal userPrincipal = (UserPrincipal) object;
                log.info("Client IP: {}, Http Method: {}, Request URI: {}, Http Status: {}, userId: {}",
                        httpRequest.getRemoteAddr(),
                        httpRequest.getMethod(),
                        requestURI,
                        httpResponse.getStatus(),
                        userPrincipal.getId()
                );

            }

            else{
                log.info("Client IP: {}, Http Method: {}, Request URI: {}, Http Status: {}",
                        httpRequest.getRemoteAddr(),
                        httpRequest.getMethod(),
                        requestURI,
                        httpResponse.getStatus()
                );
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
