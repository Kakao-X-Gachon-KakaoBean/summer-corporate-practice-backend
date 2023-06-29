package com.kakaobean.unit.controller.factory.member;

import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.member.dto.RegisterMemberRequest;

import java.time.LocalDate;

public class RegisterMemberRequestFactory {

    private RegisterMemberRequestFactory(){}

    public static RegisterMemberRequest createRequest(){
        return RegisterMemberRequest.builder()
                .name("kakoBean")
                .email("example@gmail.com")
                .password("1q2w3e4r!")
                .checkPassword("1q2w3e4r!")
                .emailAuthKey("113336")
                .build();
    }

    public static RegisterMemberResponseDto createResponseDto(){
        return new RegisterMemberResponseDto(1L);
    }
}
