package com.kakaobean.member.dto;

import com.kakaobean.core.member.application.dto.request.ModifyMemberRequestDto;

public class ModifyMemberRequest {
    //TODO: send data to core(API)

    private String nameToChange;

    public ModifyMemberRequest(String nameToChange) {
        this.nameToChange = nameToChange;
    }
    public ModifyMemberRequestDto toServiceDto(Long memberId) {
        return new ModifyMemberRequestDto(memberId, nameToChange);
    }
}
