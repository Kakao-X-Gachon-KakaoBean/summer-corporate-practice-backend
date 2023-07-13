package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class ManuscriptRegisteredEvent extends NotificationSentEvent {

    private final Long projectId;
    private final Long manuscriptId;
    private final Double version;
    private final String title;

    public ManuscriptRegisteredEvent(Long projectId,
                                     String title,
                                     List<NotificationTarget> targets,
                                     String projectTitle,
                                     Long projectId1,
                                     Long manuscriptId,
                                     Double version,
                                     String title1) {
        super(projectId, title, targets, projectTitle);
        this.projectId = projectId1;
        this.manuscriptId = manuscriptId;
        this.version = version;
        this.title = title1;
    }
}
