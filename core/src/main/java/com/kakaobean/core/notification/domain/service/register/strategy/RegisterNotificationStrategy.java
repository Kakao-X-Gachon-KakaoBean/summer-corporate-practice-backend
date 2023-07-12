package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;

public interface RegisterNotificationStrategy {
    NotificationSentEvent register(Long sourceId);
    boolean support(NotificationType notificationType);
}
