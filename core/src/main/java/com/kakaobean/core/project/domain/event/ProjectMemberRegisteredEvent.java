package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.project.domain.Project;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectMemberRegisteredEvent extends Event {

    private final Long projectId;
    private final Long memberId;

    public ProjectMemberRegisteredEvent(Long projectId, Long memberId) {
        this.projectId = projectId;
        this.memberId = memberId;
    }
}
