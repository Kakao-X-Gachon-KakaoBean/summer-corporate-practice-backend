package com.kakaobean.core.factory.member;

import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;

import java.time.LocalDate;

public class RegisterMemberServiceDtoFactory {

    private RegisterMemberServiceDtoFactory(){}

    public static RegisterMemberRequestDto createSuccessCaseRequestDto(){
        return RegisterMemberRequestDto.builder()
                .name("kakoBean")
                .age(25)
                .gender(Gender.MALE)
                .email("example@gmail.com")
                .password("1q2w3e4r!")
                .birth(LocalDate.parse("1998-03-04"))
                .emailAuthKey("113336")
                .build();
    }

    public static RegisterMemberResponseDto createResponseDto(){
        return new RegisterMemberResponseDto(1L);
    }
}
