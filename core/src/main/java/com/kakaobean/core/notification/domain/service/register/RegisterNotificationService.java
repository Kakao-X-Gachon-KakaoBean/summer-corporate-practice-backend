package com.kakaobean.core.notification.domain.service.register;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.strategy.RegisterNotificationStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RegisterNotificationService {

    private final List<RegisterNotificationStrategy> notificationStrategies;

    public RegisterNotificationService(List<RegisterNotificationStrategy> notificationStrategies) {
        this.notificationStrategies = notificationStrategies;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public NotificationSentEvent register(Long sourceId, NotificationType notificationType){
        for (RegisterNotificationStrategy notificationStrategy : notificationStrategies) {
            if(notificationStrategy.support(notificationType)){
                return notificationStrategy.register(sourceId);
            }
        }
        throw new RuntimeException("지원하는 전략 패턴이 없다.");
    }
}
