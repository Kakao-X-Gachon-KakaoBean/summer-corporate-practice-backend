package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.project.domain.ProjectRole;
import lombok.Getter;

@Getter
public class ProjectMemberRoleModifiedEvent extends Event {

    private final Long projectId;
    private final Long memberId;
    private final ProjectRole projectRole;

    public ProjectMemberRoleModifiedEvent(Long projectId, Long memberId, ProjectRole projectRole) {
        this.projectId = projectId;
        this.memberId = memberId;
        this.projectRole = projectRole;
    }
}
