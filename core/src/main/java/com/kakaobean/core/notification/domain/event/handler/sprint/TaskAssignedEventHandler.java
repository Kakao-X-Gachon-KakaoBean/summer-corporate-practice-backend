package com.kakaobean.core.notification.domain.event.handler.sprint;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.sprint.domain.event.TaskAssignedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class TaskAssignedEventHandler {

    private final RegisterNotificationService registerNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final SendMessageNotificationService sendMessageNotificationService;

    @TransactionalEventListener(TaskAssignedEventHandler.class)
    public void handler(TaskAssignedEvent event){
        NotificationSentEvent notificationSentEvent = registerNotificationService.register(event.getTaskId(), TASK);
        // 이메일 보내기
        sendEmailNotificationService.sendEmail(notificationSentEvent);
        // 알람 보내기
        sendMessageNotificationService.sendMessage(notificationSentEvent);
    }

}
