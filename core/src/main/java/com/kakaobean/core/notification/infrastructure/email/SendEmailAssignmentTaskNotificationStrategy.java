package com.kakaobean.core.notification.infrastructure.email;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationStrategy;

public class SendEmailAssignmentTaskNotificationStrategy implements SendEmailNotificationStrategy {

    @Override
    public void send(NotificationSentEvent event) {

    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {

        return false;
    }
}
