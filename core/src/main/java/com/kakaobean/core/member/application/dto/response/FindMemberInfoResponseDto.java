package com.kakaobean.core.member.application.dto.response;

import com.kakaobean.core.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class FindMemberInfoResponseDto {

    private String name;
    private String email;

    public FindMemberInfoResponseDto(String name, String email){
        this.name = name;
        this.email = email;
    }

    public static FindMemberInfoResponseDto returnInfo(Member member){
        return new FindMemberInfoResponseDto(member.getName(), member.getAuth().getEmail());
    }
}
