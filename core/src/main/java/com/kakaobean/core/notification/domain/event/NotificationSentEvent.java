package com.kakaobean.core.notification.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class NotificationSentEvent extends Event {

    private final Long projectId;
    private final String projectTitle;
    private final String targetTitle;
    private final Long targetId;

    public NotificationSentEvent(Long projectId, String projectTitle, String targetTitle, Long targetId) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.targetTitle = targetTitle;
        this.targetId = targetId;
    }
}
