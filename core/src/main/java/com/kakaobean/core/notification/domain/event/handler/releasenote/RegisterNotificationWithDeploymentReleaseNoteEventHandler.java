package com.kakaobean.core.notification.domain.event.handler.releasenote;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.releasenote.domain.event.ReleaseNoteDeployedEvent;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class RegisterNotificationWithDeploymentReleaseNoteEventHandler {

    private final ManuscriptRepository manuscriptRepository;
    private final RegisterNotificationService registerNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final SendMessageNotificationService sendMessageNotificationService;

    /**
     * 릴리즈 노트가 배포되면 발생하는 메서드
     *
     * 1. 릴리즈 노트 배포 알림을 저장한다.
     * 2. 이메일로 릴리즈 노트의 배포 알림을 발행한다.
     * 3. 메시지로 릴리즈 노트의 배포 알림을 발행한다.
     * 4. 해당 원고를 삭제한다.
     */
    //@Async 비동기로 진행하면 테스트 진행이 불가능.
    @TransactionalEventListener(value = ReleaseNoteDeployedEvent.class)
    public void handle(ReleaseNoteDeployedEvent event){
        if(event != null){
            NotificationSentEvent notificationEvent = registerNotificationService.register(event.getReleaseNoteId(), RELEASE_NOTE_DEPLOYMENT);
            sendEmailNotificationService.sendEmail(notificationEvent);
            sendMessageNotificationService.sendMessage(notificationEvent);
            manuscriptRepository.deleteByVersion(event.getVersion());
        }
    }
}