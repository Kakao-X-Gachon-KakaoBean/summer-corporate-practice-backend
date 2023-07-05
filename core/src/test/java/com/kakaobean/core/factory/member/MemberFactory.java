package com.kakaobean.core.factory.member;

import com.kakaobean.core.member.domain.*;

import java.time.LocalDate;

public class MemberFactory {

    private MemberFactory(){}

    public static Member create(){
        return Member.builder()
                .id(1L)
                .name("kakoBean")
                .auth(new Auth("example@gmail.com", "1q2w3e4r!"))
                .role(Role.ROLE_USER)
                .authProvider(AuthProvider.local)
                .build();
    }

    public static Member createWithoutId(){
        return Member.builder()
                .name("kakoBean1")
                .auth(new Auth("example1@gmail.com", "1q2w3e4r!"))
                .role(Role.ROLE_USER)
                .authProvider(AuthProvider.local)
                .build();
    }
}
