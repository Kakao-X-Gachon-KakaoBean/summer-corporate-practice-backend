package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.util.List;

@Getter
public class SendDeploymentReleaseNoteNotificationEvent extends SendNotificationEvent {

    private Long releaseNoteId;

    public SendDeploymentReleaseNoteNotificationEvent(Long projectId,
                                                      String title,
                                                      List<String> emails,
                                                      Long releaseNoteId,
                                                      String projectTitle) {
        super(projectId, title, emails, projectTitle);
        this.releaseNoteId = releaseNoteId;
    }
}
