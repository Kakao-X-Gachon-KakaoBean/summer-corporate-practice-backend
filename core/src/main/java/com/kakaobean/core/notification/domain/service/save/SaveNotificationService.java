package com.kakaobean.core.notification.domain.service.save;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.SendNotificationEvent;
import com.kakaobean.core.notification.domain.service.save.strategy.SaveNotificationStrategy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SaveNotificationService {

    private final List<SaveNotificationStrategy> notificationStrategies;

    public SaveNotificationService(List<SaveNotificationStrategy> notificationStrategies) {
        this.notificationStrategies = notificationStrategies;
    }


    @Transactional
    public SendNotificationEvent save(Long sourceId, NotificationType notificationType){
        for (SaveNotificationStrategy notificationStrategy : notificationStrategies) {
            if(notificationStrategy.support(notificationType)){
                return notificationStrategy.save(sourceId);
            }
        }
        throw new RuntimeException("지원하는 전략 패턴이 없다.");
    }
}
