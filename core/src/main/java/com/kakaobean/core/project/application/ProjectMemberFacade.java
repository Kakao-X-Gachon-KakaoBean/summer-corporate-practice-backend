package com.kakaobean.core.project.application;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;

import org.springframework.stereotype.Component;


@Component
public class ProjectMemberFacade {

    private final ProjectMemberService projectMemberService;
    private final InvitationProjectMemberService invitationProjectMemberService;

    public ProjectMemberFacade(ProjectMemberService projectMemberService,
                               InvitationProjectMemberService invitationProjectMemberService) {
        this.projectMemberService = projectMemberService;
        this.invitationProjectMemberService = invitationProjectMemberService;
    }
    public void inviteProjectMembers(InviteProjectMemberRequestDto dto){
        ProjectMemberInvitedEvent event = projectMemberService.registerInvitedProjectPersons(dto);
        invitationProjectMemberService.sendInvitationMails(event.getInvitedMemberEmails(), event.getProject());
    }
}
