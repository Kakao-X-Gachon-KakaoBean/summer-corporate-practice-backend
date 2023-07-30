package com.kakaobean.core.notification.infrastructure.email;

import com.kakaobean.common.EmailHtmlUtils;
import com.kakaobean.core.notification.domain.event.AssignmentTaskNotificationEvent;
import com.kakaobean.core.notification.domain.event.ModifiedProjectMemberNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationStrategy;
import com.kakaobean.independentlysystem.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendEmailAssignmentTaskNotificationStrategy implements SendEmailNotificationStrategy {

    private final EmailSender emailSender;

    @Override
    public void send(NotificationSentEvent event) {

        AssignmentTaskNotificationEvent notificationEvent = (AssignmentTaskNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 프로젝트 작업 할당 안내입니다.";
        String url = "localhost:3000" + notificationEvent.getUrl();
        emailSender.sendEmail(
                List.of(notificationEvent.getEmail()),
                title,
                () -> EmailHtmlUtils.makeLinkHtml(
                        "작업 할당",
                        notificationEvent.getContent(),
                        "",
                        "아래 링크",
                        "에서 확인하실 수 있습니다.",
                        "링크",
                        url
                )
        );
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == AssignmentTaskNotificationEvent.class;
    }
}
