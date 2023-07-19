package com.kakaobean.core.notification.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public abstract class NotificationSentEvent extends Event {

    private final String url;
    private final String projectTitle;
    private final String content;
    private final LocalDateTime issuedAt;

    public NotificationSentEvent(String url,
                                 String projectTitle,
                                 String content,
                                 LocalDateTime localDateTime) {
        this.url = url;
        this.projectTitle = projectTitle;
        this.content = content;
        this.issuedAt = localDateTime;
    }
}
