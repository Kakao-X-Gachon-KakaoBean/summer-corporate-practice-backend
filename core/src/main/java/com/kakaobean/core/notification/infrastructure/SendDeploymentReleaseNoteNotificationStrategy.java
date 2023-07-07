package com.kakaobean.core.notification.infrastructure;

import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.SendNotificationEvent;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationStrategy;
import com.kakaobean.core.notification.utils.ReleaseNoteDeploymentNotificationEmailUtils;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendDeploymentReleaseNoteNotificationStrategy implements SendEmailNotificationStrategy {

    private final EmailSender emailSender;

    public SendDeploymentReleaseNoteNotificationStrategy(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(SendNotificationEvent event) {
        SendDeploymentReleaseNoteNotificationEvent notificationEvent = (SendDeploymentReleaseNoteNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 프로젝트 릴리즈 노트 배포 안내입니다.";
        String url = "localhost:3000/projects/" + notificationEvent.getProjectId() + "/release-notes/" + notificationEvent.getReleaseNoteId();
        emailSender.sendEmail(
                event.getEmails(),
                title,
                () -> ReleaseNoteDeploymentNotificationEmailUtils.getHtml(notificationEvent.getTitle(), url)
        );

    }

    @Override
    public boolean support(Class<? extends SendNotificationEvent> eventClass) {
        return eventClass == SendDeploymentReleaseNoteNotificationEvent.class;
    }
}
