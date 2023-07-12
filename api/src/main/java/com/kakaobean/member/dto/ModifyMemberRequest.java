package com.kakaobean.member.dto;

import com.kakaobean.core.member.application.dto.request.ModifyMemberRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyMemberRequest {

    private String nameToChange;

    public ModifyMemberRequest(String nameToChange) {
        this.nameToChange = nameToChange;
    }
    public ModifyMemberRequestDto toServiceDto(Long memberId) {
        return new ModifyMemberRequestDto(memberId, nameToChange);
    }
}
