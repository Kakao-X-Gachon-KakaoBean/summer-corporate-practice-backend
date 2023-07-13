package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.util.List;

@Getter
public class SendDeploymentReleaseNoteNotificationEvent extends NotificationSentEvent {

    private final List<String> emails;

    public SendDeploymentReleaseNoteNotificationEvent(Long projectId,
                                                      String targetTitle,
                                                      Long targetId,
                                                      String projectTitle,
                                                      List<String> emails) {
        super(projectId, projectTitle, targetTitle, targetId);
        this.emails = emails;
    }
}
