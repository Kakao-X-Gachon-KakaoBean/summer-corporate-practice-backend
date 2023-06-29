package com.kakaobean.core.member.domain.service;

public interface VerifiedEmailService {
    void sendVerificationEmail(String receiveEmail);
    void verifyAuthEmailKey(String email, String authKey);
}
