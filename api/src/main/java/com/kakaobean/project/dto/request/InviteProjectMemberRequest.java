package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectMemberRequest {

    private List<String> invitedMemberEmails;

    public InviteProjectMemberRequest() {}

    public InviteProjectMemberRequest(List<String> invitedMemberEmails) {
        this.invitedMemberEmails = invitedMemberEmails;
    }

    public InviteProjectMemberRequestDto toServiceDto(Long projectId, Long projectAdminId){
        return new InviteProjectMemberRequestDto(invitedMemberEmails, projectId, projectAdminId);
    }
}
