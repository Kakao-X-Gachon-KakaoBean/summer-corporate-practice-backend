package com.kakaobean.core.notification.domain.event;

import java.time.LocalDateTime;

public class AssignmentTaskNotificationEvent extends NotificationSentEvent{



    public AssignmentTaskNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime) {
        super(url, projectTitle, content, localDateTime);
    }
}
