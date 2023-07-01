package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.project.domain.Project;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectMemberInvitedEvent extends Event {

    private List<String> invitedMemberEmails;
    private Project project;

    public ProjectMemberInvitedEvent(List<String> invitedMemberEmails, Project project) {
        this.invitedMemberEmails = invitedMemberEmails;
        this.project = project;
    }
}
