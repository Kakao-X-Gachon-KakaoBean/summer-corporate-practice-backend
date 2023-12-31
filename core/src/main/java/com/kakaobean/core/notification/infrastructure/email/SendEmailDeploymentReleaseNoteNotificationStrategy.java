package com.kakaobean.core.notification.infrastructure.email;

import com.kakaobean.common.EmailHtmlUtils;
import com.kakaobean.core.notification.config.EmailProperties;
import com.kakaobean.core.notification.domain.event.DeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationStrategy;
import com.kakaobean.independentlysystem.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailDeploymentReleaseNoteNotificationStrategy implements SendEmailNotificationStrategy {

    private final EmailSender emailSender;
    private final EmailProperties emailProperties;

    @Override
    public void send(NotificationSentEvent event) {
        DeploymentReleaseNoteNotificationEvent notificationEvent = (DeploymentReleaseNoteNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 프로젝트 릴리즈 노트 배포 안내입니다.";
        String url = emailProperties.getHostName() + notificationEvent.getUrl();
        emailSender.sendEmail(
                notificationEvent.getEmails(),
                title,
                () -> EmailHtmlUtils.makeLinkHtml(
                        "릴리즈 노트 배포",
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
        return eventClass == DeploymentReleaseNoteNotificationEvent.class;
    }
}
