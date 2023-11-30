package com.kakaobean.fixture.member;

import com.kakaobean.core.member.domain.Auth;
import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.Role;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MemberFactory {

    private MemberFactory() {}

    private static AtomicInteger aInteger = new AtomicInteger(1);

    public static Member createAdminWithTempEmail() {
        String random = getRandomString();
        return Member.builder()
                .auth(new Auth(random + "@gmail.com", "1q2w3e4r!"))
                .authProvider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .name(random + "name")
                .build();
    }

    private static String getRandomString() {
        int i = aInteger.getAndIncrement();
        String uuid = UUID.randomUUID().toString();
        String random = uuid.substring(uuid.length() - 12) + i;
        return random;
    }

    public static Member createMemberWithTempEmail() {
        String random = getRandomString();
        return Member.builder()
                .auth(new Auth(random + "tester@gmail.com", "1q2w3e4r!"))
                .authProvider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .name(random + "name")
                .build();
    }


    public static Member createAdminWithRealEmail() {
        return Member.builder()
                .auth(new Auth("dlsdlaqja888@gmail.com", "1q2w3e4r!"))
                .authProvider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .name("name1")
                .build();
    }

    public static Member createMemberWithRealEmail() {
        return Member.builder()
                .auth(new Auth("asb1651@naver.com", "1q2w3e4r!"))
                .authProvider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .name("name2")
                .build();
    }

    /**
     * 쓰레드 로컬을 지우고 재사용할 때 활용
     */
    public static Member createWithId(Long memberId, Member member) {
        return Member.builder()
                .id(memberId)
                .auth(new Auth(member.getAuth().getEmail(), member.getAuth().getPassword()))
                .authProvider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .name(member.getName())
                .build();
    }
}
