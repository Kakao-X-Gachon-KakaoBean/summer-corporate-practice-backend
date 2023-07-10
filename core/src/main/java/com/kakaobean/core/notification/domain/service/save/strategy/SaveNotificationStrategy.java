package com.kakaobean.core.notification.domain.service.save.strategy;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.SendNotificationEvent;

public interface SaveNotificationStrategy {
    SendNotificationEvent save(Long sourceId);
    boolean support(NotificationType notificationType);
}
