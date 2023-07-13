package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import lombok.Getter;

@Getter
public class ManuscriptRegisteredEvent extends NotificationSentEvent {

    private final Long projectId;
    private final Long manuscriptId;
    private final Double version;
    private final String title;

    public ManuscriptRegisteredEvent(Long projectId, Long manuscriptId, Double version, String title) {
        super();
        this.projectId = projectId;
        this.manuscriptId = manuscriptId;
        this.version = version;
        this.title = title;
    }
}
