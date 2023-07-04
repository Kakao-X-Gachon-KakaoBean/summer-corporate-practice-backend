package com.kakaobean.core.project.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectMemberRequestDto {

    private final List<String> invitedMemberEmails;
    private final Long projectId;
    private final Long projectAdminId;

    public InviteProjectMemberRequestDto(List<String> invitedMemberEmails, Long projectId, Long projectAdminId) {
        this.invitedMemberEmails = invitedMemberEmails;
        this.projectId = projectId;
        this.projectAdminId = projectAdminId;
    }
}
