package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegisterManuscriptNotificationEvent extends NotificationSentEvent {

    private final Long projectId;

    public RegisterManuscriptNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime, Long projectId) {
        super(url, projectTitle, content, localDateTime);
        this.projectId = projectId;
    }
}
