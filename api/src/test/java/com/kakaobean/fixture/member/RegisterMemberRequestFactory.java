package com.kakaobean.fixture.member;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.member.dto.RegisterMemberRequest;

import java.util.concurrent.atomic.AtomicLong;


public class RegisterMemberRequestFactory {

    private RegisterMemberRequestFactory(){}

    private static AtomicLong aLong = new AtomicLong(0);

    /**
     * 단위테스트에서만 사용
     */
    public static RegisterMemberRequest createAdmin(){
        return RegisterMemberRequest.builder()
                .name("kakoBean")
                .email("asb1651@naver.com")
                .password("1q2w3e4r!")
                .checkPassword("1q2w3e4r!")
                .emailAuthKey("113336")
                .build();
    }


    public static RegisterMemberRequest createMember(Member member){
        return RegisterMemberRequest.builder()
                .name(member.getName())
                .email(member.getAuth().getEmail())
                .password(member.getAuth().getPassword())
                .checkPassword(member.getAuth().getPassword())
                .emailAuthKey("113339")
                .build();
    }
}
