package com.kakaobean.core.project.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectMemberRequestDto {

    private List<Long> invitedMemberIdList;
    private Long projectId;
    private Long projectAdminId;


    public InviteProjectMemberRequestDto(List<Long> invitedMemberIdList, Long projectId, Long projectAdminId) {
        this.invitedMemberIdList = invitedMemberIdList;
        this.projectId = projectId;
        this.projectAdminId = projectAdminId;
    }
}
