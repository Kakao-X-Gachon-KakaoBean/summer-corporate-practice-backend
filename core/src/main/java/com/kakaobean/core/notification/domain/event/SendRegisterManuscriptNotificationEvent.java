package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

@Getter
public class SendRegisterManuscriptNotificationEvent extends NotificationSentEvent {

    private final Double version;

    public SendRegisterManuscriptNotificationEvent(Long projectId, String projectTitle, String targetTitle, Long targetId, Double version) {
        super(projectId, projectTitle, targetTitle, targetId);
        this.version = version;
    }
}
