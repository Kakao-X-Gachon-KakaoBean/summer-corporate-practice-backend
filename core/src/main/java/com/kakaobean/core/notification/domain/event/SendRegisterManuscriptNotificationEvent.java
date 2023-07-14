package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendRegisterManuscriptNotificationEvent extends NotificationSentEvent {

    private final Long projectId;

    public SendRegisterManuscriptNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime, Long projectId) {
        super(url, projectTitle, content, localDateTime);
        this.projectId = projectId;
    }
}
