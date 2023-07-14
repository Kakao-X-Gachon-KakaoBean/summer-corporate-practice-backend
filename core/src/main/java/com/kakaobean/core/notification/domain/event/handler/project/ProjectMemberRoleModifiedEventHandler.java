package com.kakaobean.core.notification.domain.event.handler.project;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.project.domain.event.ProjectMemberRoleModifiedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class ProjectMemberRoleModifiedEventHandler {

    private final RegisterNotificationService registerNotificationService;
    private final SendEmailNotificationService sendEmailNotificationService;
    private final SendMessageNotificationService sendMessageNotificationService;

    /**
     * 프로젝트 멤버의 권한이 바뀌었을 때 발생하는 메서드
     *
     * 권한이 수정된 멤버에게 메시지와 이메일이 알림이 가야한다.
     * 알림은 저장되어야 한다.
     */
    @TransactionalEventListener(ProjectMemberRoleModifiedEvent.class)
    public void handle(ProjectMemberRoleModifiedEvent event) {
        NotificationSentEvent notificationSentEvent = registerNotificationService.register(event.getProjectMemberId(), MODIFIED_PROJECT_MEMBER_ROLE);
        sendEmailNotificationService.sendEmail(notificationSentEvent);
        sendMessageNotificationService.sendMessage(notificationSentEvent);
    }
}
