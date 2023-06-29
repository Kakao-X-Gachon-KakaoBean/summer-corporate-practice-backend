package com.kakaobean.core.project.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectMemberRequestDto {

    private List<Long> invitedMemberIdList;

    public InviteProjectMemberRequestDto(List<Long> invitedMemberIdList) {
        this.invitedMemberIdList = invitedMemberIdList;
    }
}
