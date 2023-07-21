package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StartManuscriptModificationNotificationEvent extends NotificationSentEvent {

    private final Long projectId;

    public StartManuscriptModificationNotificationEvent(String url, String projectTitle, String content, LocalDateTime issuedAt, Long projectId) {
        super(url, projectTitle, content, issuedAt);
        this.projectId = projectId;
    }
}
