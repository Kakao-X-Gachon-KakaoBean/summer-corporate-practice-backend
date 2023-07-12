package com.kakaobean.core.notification.domain.service.send.message;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendMessageNotificationService {

    private final List<SendMessageNotificationStrategy> strategies;

    public SendMessageNotificationService(List<SendMessageNotificationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void sendMessage(NotificationSentEvent event){
        for (SendMessageNotificationStrategy strategy : strategies) {
            if(strategy.support(event.getClass())){
                strategy.send(event);
            }
        }
    }
}
