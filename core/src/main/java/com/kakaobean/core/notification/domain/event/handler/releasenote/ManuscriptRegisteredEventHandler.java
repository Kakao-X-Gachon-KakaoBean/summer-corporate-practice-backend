package com.kakaobean.core.notification.domain.event.handler.releasenote;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.releasenote.domain.event.ManuscriptRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.REGISTER_MANUSCRIPT;

@Component
@RequiredArgsConstructor
public class ManuscriptRegisteredEventHandler {

    private final SendMessageNotificationService sendMessageNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final RegisterNotificationService registerNotificationService;

    /**
     * 릴리즈 노트 원고가 등록되면, 실시간 알림이 보내진다. 알림은 저장되야 한다.
     */
    @TransactionalEventListener(ManuscriptRegisteredEvent.class)
    public void handle(ManuscriptRegisteredEvent event) {
        if(event != null){
            NotificationSentEvent notificationEvent = registerNotificationService.register(event.getManuscriptId(), REGISTER_MANUSCRIPT);
            sendEmailNotificationService.sendEmail(notificationEvent);
            sendMessageNotificationService.sendMessage(notificationEvent);
        }
    }
}
