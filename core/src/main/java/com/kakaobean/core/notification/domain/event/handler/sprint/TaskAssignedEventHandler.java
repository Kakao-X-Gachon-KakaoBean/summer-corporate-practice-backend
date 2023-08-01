package com.kakaobean.core.notification.domain.event.handler.sprint;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.sprint.domain.event.TaskAssignedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.TASK;

@Component
@RequiredArgsConstructor
public class TaskAssignedEventHandler {

    private final RegisterNotificationService registerNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final SendMessageNotificationService sendMessageNotificationService;

    @TransactionalEventListener(TaskAssignedEvent.class)
    public void handler(TaskAssignedEvent event){
        if(event != null){
            NotificationSentEvent notificationSentEvent = registerNotificationService.register(event.getTaskId(), TASK);
            sendEmailNotificationService.sendEmail(notificationSentEvent);
            sendMessageNotificationService.sendMessage(notificationSentEvent);
        }
    }

}
