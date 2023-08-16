package com.kakaobean.core.issue.domain.event;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.POSTED_COMMENT;

@Configuration
@RequiredArgsConstructor
public class RegisterCommentEventHandler {

    private final SendMessageNotificationService sendMessageNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final RegisterNotificationService registerNotificationService;


    @TransactionalEventListener(RegisterCommentEvent.class)
    public void handler(RegisterCommentEvent event){
        if(event != null){
            NotificationSentEvent notificationEvent = registerNotificationService.register(event.getCommentId(), POSTED_COMMENT);
            sendEmailNotificationService.sendEmail(notificationEvent);
            sendMessageNotificationService.sendMessage(notificationEvent);
        }
    }
}
