package com.kakaobean.core.project.application;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;

import org.springframework.stereotype.Component;

import java.util.List;

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
        List<ProjectMemberInvitedEvent> events = projectMemberService.inviteProjectMembers(dto);
        sendInvitationProjectEmails(events);
    }

    private void sendInvitationProjectEmails(List<ProjectMemberInvitedEvent> events) {
        for (ProjectMemberInvitedEvent event : events) {
            invitationProjectMemberService.sendInvitationMail(event.getInvitedMemberId(), event.getProject());
        }
    }
}
