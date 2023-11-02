package com.kakaobean.fixture.member;

import com.kakaobean.core.member.domain.Auth;
import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.Role;

import java.util.concurrent.atomic.AtomicInteger;

public class MemberFactory {


    private MemberFactory() {}

    private static AtomicInteger aInteger = new AtomicInteger(1);

    public static Member createWithTempEmail() {
        int i = aInteger.getAndIncrement();
        return Member.builder()
                .auth(new Auth(i + "tester@gmail.com", "1q2w3e4r!"))
                .authProvider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .name(i + "name")
                .build();
    }
}
