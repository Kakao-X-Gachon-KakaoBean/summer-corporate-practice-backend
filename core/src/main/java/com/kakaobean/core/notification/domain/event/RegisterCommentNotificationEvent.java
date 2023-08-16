package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegisterCommentNotificationEvent extends NotificationSentEvent{

    private final Long issueId;

    private final Long projectId;

    private final String email;
    private final Long writerId;

    public RegisterCommentNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime, String email, Long writerId, Long issueId, Long projectId) {
        super(url, projectTitle, content, localDateTime);
        this.projectId = projectId;
        this.issueId = issueId;
        this.email = email;
        this.writerId = writerId;
    }

}
