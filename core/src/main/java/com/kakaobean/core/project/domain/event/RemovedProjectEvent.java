package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class RemovedProjectEvent extends Event {

    private final Long projectId;

    public RemovedProjectEvent(Long projectId) {
        this.projectId = projectId;
    }
}
