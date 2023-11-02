package com.kakaobean.fixture.member;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.member.dto.RegisterMemberRequest;

import java.util.concurrent.atomic.AtomicLong;

import static com.kakaobean.acceptance.TestMember.*;

public class RegisterMemberRequestFactory {

    private RegisterMemberRequestFactory(){}

    private static AtomicLong aLong = new AtomicLong(0);

    public static RegisterMemberRequest createAdmin(){
        return RegisterMemberRequest.builder()
                .name("kakoBean")
                .email(ADMIN.getEmail())
                .password(ADMIN.getPassword())
                .checkPassword(ADMIN.getPassword())
                .emailAuthKey("113336")
                .build();
    }
    public static RegisterMemberRequest createMember(){
        return RegisterMemberRequest.builder()
                .name("receiver")
                .email(MEMBER.getEmail())
                .password(MEMBER.getPassword())
                .checkPassword(MEMBER.getPassword())
                .emailAuthKey("113335")
                .build();
    }
    public static RegisterMemberRequest createMember(Member member){
        long l = aLong.getAndIncrement();
        return RegisterMemberRequest.builder()
                .name(member.getName())
                .email(member.getAuth().getEmail())
                .password(member.getAuth().getPassword())
                .checkPassword(member.getAuth().getPassword())
                .emailAuthKey("113339")
                .build();
    }
}
