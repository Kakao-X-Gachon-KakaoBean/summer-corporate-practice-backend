package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AssignmentTaskNotificationEvent extends NotificationSentEvent{

    private final String email;
    private final Long workerId;

    public AssignmentTaskNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime, String email, Long workerId) {
        super(url, projectTitle, content, localDateTime);
        this.email = email;
        this.workerId = workerId;
    }

}
