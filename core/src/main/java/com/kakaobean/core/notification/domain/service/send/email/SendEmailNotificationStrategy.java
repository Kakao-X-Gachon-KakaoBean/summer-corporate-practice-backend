package com.kakaobean.core.notification.domain.service.send.email;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;

public interface SendEmailNotificationStrategy {
    void send(NotificationSentEvent event);
    boolean support(Class<? extends NotificationSentEvent> eventClass);
}
