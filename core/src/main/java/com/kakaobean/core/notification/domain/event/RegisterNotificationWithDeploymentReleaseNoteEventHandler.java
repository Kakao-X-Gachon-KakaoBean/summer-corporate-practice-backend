package com.kakaobean.core.notification.domain.event;

import com.kakaobean.core.notification.domain.service.save.SaveNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class RegisterNotificationWithDeploymentReleaseNoteEventHandler {

    private final SaveNotificationService saveNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;

    //@Async 비동기로 진행하면 테스트 진행이 불가능.
    @TransactionalEventListener(value = ReleaseNoteRegisteredEvent.class)
    public void handle(ReleaseNoteRegisteredEvent event){
        if(event != null){
            SendNotificationEvent notificationEvent = saveNotificationService.save(event.getReleaseNoteId(), RELEASE_NOTE_DEPLOYMENT);
            sendEmailNotificationService.sendEmail(notificationEvent);
            //TODO amqp도 사용
        }
    }
}