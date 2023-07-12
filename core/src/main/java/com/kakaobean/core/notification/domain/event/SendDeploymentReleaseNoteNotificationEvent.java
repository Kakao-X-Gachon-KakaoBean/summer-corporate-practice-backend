package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.util.List;

@Getter
public class SendDeploymentReleaseNoteNotificationEvent extends NotificationSentEvent {

    private final Long releaseNoteId;

    public SendDeploymentReleaseNoteNotificationEvent(Long projectId,
                                                      String title,
                                                      List<NotificationTarget> infos,
                                                      String projectTitle,
                                                      Long releaseNoteId) {
        super(projectId, title, infos, projectTitle);
        this.releaseNoteId = releaseNoteId;
    }
}
