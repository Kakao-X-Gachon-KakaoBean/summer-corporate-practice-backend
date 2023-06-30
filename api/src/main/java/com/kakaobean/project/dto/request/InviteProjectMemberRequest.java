package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectMemberRequest {

    private List<Long> invitedMemberIdList;

    public InviteProjectMemberRequest() {}

    public InviteProjectMemberRequest(List<Long> invitedMemberIdList) {
        this.invitedMemberIdList = invitedMemberIdList;
    }

    public InviteProjectMemberRequestDto toServiceDto(Long projectId, Long projectAdminId){
        return new InviteProjectMemberRequestDto(invitedMemberIdList, projectId, projectAdminId);
    }

}
