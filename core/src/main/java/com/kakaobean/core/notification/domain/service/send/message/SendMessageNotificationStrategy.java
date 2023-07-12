package com.kakaobean.core.notification.domain.service.send.message;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;

public interface SendMessageNotificationStrategy {
    void send(NotificationSentEvent event);
    boolean support(Class<? extends NotificationSentEvent> eventClass);
}
