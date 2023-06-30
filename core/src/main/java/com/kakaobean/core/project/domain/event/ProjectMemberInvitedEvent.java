package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.project.domain.Project;
import lombok.Getter;

@Getter
public class ProjectMemberInvitedEvent extends Event {

    private Long invitedMemberId;
    private Project project;

    public ProjectMemberInvitedEvent(Long invitedMemberId, Project project) {
        this.invitedMemberId = invitedMemberId;
        this.project = project;
    }
}
