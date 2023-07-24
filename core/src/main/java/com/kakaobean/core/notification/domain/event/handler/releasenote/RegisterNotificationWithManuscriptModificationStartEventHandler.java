package com.kakaobean.core.notification.domain.event.handler.releasenote;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.releasenote.domain.event.ManuscriptModificationStartedEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.START_MANUSCRIPT_MODIFICATION;

@Component
@RequiredArgsConstructor
public class RegisterNotificationWithManuscriptModificationStartEventHandler {

    private final RegisterNotificationService registerNotificationService;
    private final SendMessageNotificationService sendMessageNotificationService;

    @TransactionalEventListener(ManuscriptModificationStartedEvent.class)
    public void handle(ManuscriptModificationStartedEvent event) {
        NotificationSentEvent notificationSentEvent = registerNotificationService.register(event.getManuscriptId(), START_MANUSCRIPT_MODIFICATION);
        sendMessageNotificationService.sendMessage(notificationSentEvent);
    }
}
