package com.kakaobean.core.member.domain.repository;

import com.kakaobean.core.member.domain.Email;

public interface EmailRepository {
    void save(Email email);
    Email getEmailCertification(Email email);
    void removeEmailCertification(Email email);
    boolean hasKey(Email email);
}
