package com.kakaobean.core.notification.domain.service.send.email;

import com.kakaobean.core.notification.domain.event.NotificationSendedEvent;

public interface SendEmailNotificationStrategy {
    void send(NotificationSendedEvent event);
    boolean support(Class<? extends NotificationSendedEvent> eventClass);
}
