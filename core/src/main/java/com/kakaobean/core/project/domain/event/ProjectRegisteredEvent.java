package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class ProjectRegisteredEvent extends Event {

    private Long projectId;
    private Long projectAdminId;

    public ProjectRegisteredEvent(Long projectId, Long proejectAdminId) {
        this.projectId = projectId;
        this.projectAdminId = proejectAdminId;
    }
}
