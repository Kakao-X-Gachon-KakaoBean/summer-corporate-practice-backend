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
    private String profileImg;
    private String thumbnailImg;

    public FindMemberInfoResponseDto(String name, String email, String profileImg, String thumbnailImg) {
        this.name = name;
        this.email = email;
        this.profileImg = profileImg;
        this.thumbnailImg = thumbnailImg;
    }

    public static FindMemberInfoResponseDto returnInfo(Member member){
        return new FindMemberInfoResponseDto(member.getName(), member.getAuth().getEmail(), member.getProfileImg(), member.getThumbnailImg());
    }
}
