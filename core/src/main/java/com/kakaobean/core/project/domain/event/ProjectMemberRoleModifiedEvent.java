package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.project.domain.ProjectRole;
import lombok.Getter;

@Getter
public class ProjectMemberRoleModifiedEvent extends Event {

    private final Long projectMemberId;

    public ProjectMemberRoleModifiedEvent(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
    }
}
