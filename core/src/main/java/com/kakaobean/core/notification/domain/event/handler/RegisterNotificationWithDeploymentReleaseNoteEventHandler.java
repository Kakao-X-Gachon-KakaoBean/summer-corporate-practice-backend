package com.kakaobean.core.notification.domain.event.handler;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.releasenote.domain.ReleaseNoteDeployedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class RegisterNotificationWithDeploymentReleaseNoteEventHandler {

    private final RegisterNotificationService registerNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final SendMessageNotificationService sendMessageNotificationService;

    //@Async 비동기로 진행하면 테스트 진행이 불가능.
    @TransactionalEventListener(value = ReleaseNoteDeployedEvent.class)
    public void handle(ReleaseNoteDeployedEvent event){
        if(event != null){
            NotificationSentEvent notificationEvent = registerNotificationService.register(event.getReleaseNoteId(), RELEASE_NOTE_DEPLOYMENT);
            sendEmailNotificationService.sendEmail(notificationEvent);
            sendMessageNotificationService.sendMessage(notificationEvent);
        }
    }
}