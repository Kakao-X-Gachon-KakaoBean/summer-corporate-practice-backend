package com.kakaobean.core.factory.member;

import com.kakaobean.core.member.domain.*;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MemberFactory {

    private static AtomicLong i = new AtomicLong(500);

    private MemberFactory(){}


    public static Long getMemberId() {
        return i.getAndIncrement();
    }

    public static Member create(){
        long value = i.getAndIncrement();
        return Member.builder()
                .id(1L)
                .name("kakoBean" + value)
                .auth(new Auth(value + "example@gmail.com", "1q2w3e4r!"))
                .role(Role.ROLE_USER)
                .authProvider(AuthProvider.local)
                .build();
    }

    public static Member createWithoutId(){
        long value = i.getAndIncrement();
        return Member.builder()
                .name("kakoBean" + value)
                .auth(new Auth(value + "example1@gmail.com", "1q2w3e4r!"))
                .role(Role.ROLE_USER)
                .authProvider(AuthProvider.local)
                .build();
    }
}
