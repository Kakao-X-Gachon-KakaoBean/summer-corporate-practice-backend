package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DeploymentReleaseNoteNotificationEvent extends NotificationSentEvent {

    private final List<String> emails;
    private final Long projectId;

    public DeploymentReleaseNoteNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime, List<String> emails, Long projectId) {
        super(url, projectTitle, content, localDateTime);
        this.emails = emails;
        this.projectId = projectId;
    }
}
