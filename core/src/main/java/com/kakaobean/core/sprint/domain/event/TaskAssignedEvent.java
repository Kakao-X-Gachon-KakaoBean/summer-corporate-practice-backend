package com.kakaobean.core.sprint.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class TaskAssignedEvent extends Event {

    private final Long taskId;
    private final Long sprintId;
    private final Long workerId;

    public TaskAssignedEvent(Long taskId, Long sprintId, Long workerId) {
        this.taskId = taskId;
        this.sprintId = sprintId;
        this.workerId = workerId;
    }
}
