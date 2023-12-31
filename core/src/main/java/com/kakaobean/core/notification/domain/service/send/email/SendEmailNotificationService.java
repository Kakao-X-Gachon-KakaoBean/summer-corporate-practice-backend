package com.kakaobean.core.notification.domain.service.send.email;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendEmailNotificationService {

    private final List<SendEmailNotificationStrategy> strategies;

    public SendEmailNotificationService(List<SendEmailNotificationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void sendEmail(NotificationSentEvent event){
        for (SendEmailNotificationStrategy strategy : strategies) {
            if(strategy.support(event.getClass())){
                strategy.send(event);
            }
        }
    }
}
