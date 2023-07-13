package com.kakaobean.core.notification.domain.event.handler;

import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.releasenote.domain.event.ManuscriptRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ManuscriptRegisteredEventHandler {

    private final SendMessageNotificationService sendMessageNotificationService;

    @TransactionalEventListener(ManuscriptRegisteredEvent.class)
    public void handle(ManuscriptRegisteredEvent event) {
        if(event != null){
            //sendMessageNotificationService.sendMessage();
        }
    }
}
