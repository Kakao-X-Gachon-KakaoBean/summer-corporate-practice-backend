package com.kakaobean.core.member.application.dto.request;

import lombok.Getter;

@Getter
public class ModifyMemberRequestDto {

    private Long memberId;
    private String nameToChange;

    public ModifyMemberRequestDto(Long memberId, String nameToChange) {
        this.memberId = memberId;
        this.nameToChange = nameToChange;
    }
}
