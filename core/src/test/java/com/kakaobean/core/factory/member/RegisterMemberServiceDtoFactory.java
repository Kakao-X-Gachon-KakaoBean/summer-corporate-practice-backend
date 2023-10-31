package com.kakaobean.core.factory.member;

import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class RegisterMemberServiceDtoFactory {

    private static AtomicInteger i = new AtomicInteger(0);

    private RegisterMemberServiceDtoFactory(){}

    public static RegisterMemberRequestDto createSuccessCaseRequestDto(){
        int value = i.getAndIncrement();
        return RegisterMemberRequestDto.builder()
                .name("kakoBean" + value)
                .email("example@gmail.com" + value)
                .password("1q2w3e4r!")
                .emailAuthKey("113336")
                .build();
    }

    public static RegisterMemberResponseDto createResponseDto(){
        return new RegisterMemberResponseDto(1L);
    }
}
