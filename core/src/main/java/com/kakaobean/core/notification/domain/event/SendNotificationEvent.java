package com.kakaobean.core.notification.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class SendNotificationEvent extends Event {

    private final Long projectId;
    private final String title;
    private final List<String> emails;
    private final String projectTitle;


    public SendNotificationEvent(Long projectId,
                                 String title,
                                 List<String> emails,
                                 String projectTitle) {
        this.projectId = projectId;
        this.title = title;
        this.emails = emails;
        this.projectTitle = projectTitle;
    }
}
