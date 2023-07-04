package com.kakaobean.unit.controller.factory.member;

import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.member.dto.RegisterMemberRequest;

import static com.kakaobean.acceptance.TestMember.*;

public class RegisterMemberRequestFactory {

    private RegisterMemberRequestFactory(){}

    public static RegisterMemberRequest createRequest(){
        return RegisterMemberRequest.builder()
                .name("kakoBean")
                .email(MEMBER.getEmail())
                .password(MEMBER.getPassword())
                .checkPassword(MEMBER.getPassword())
                .emailAuthKey("113336")
                .build();
    }

    public static RegisterMemberResponseDto createResponseDto(){
        return new RegisterMemberResponseDto(1L);
    }
}
