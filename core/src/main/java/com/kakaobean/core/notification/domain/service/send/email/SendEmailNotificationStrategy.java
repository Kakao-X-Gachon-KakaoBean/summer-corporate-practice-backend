package com.kakaobean.core.notification.domain.service.send.email;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.SendNotificationEvent;

public interface SendEmailNotificationStrategy {
    void send(SendNotificationEvent event);
    boolean support(Class<? extends SendNotificationEvent> eventClass);
}
