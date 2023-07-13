package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

@Getter
public class SendRegisterManuscriptNotificationEvent extends NotificationSentEvent {

    public SendRegisterManuscriptNotificationEvent(Long projectId, String projectTitle, String targetTitle, Long targetId) {
        super(projectId, projectTitle, targetTitle, targetId);
    }
}
