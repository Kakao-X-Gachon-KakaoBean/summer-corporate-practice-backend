package com.kakaobean.core.notification.infrastructure;

import com.kakaobean.common.EmailHtmlUtils;
import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationStrategy;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SendEmailDeploymentReleaseNoteNotificationStrategy implements SendEmailNotificationStrategy {

    private final EmailSender emailSender;

    public SendEmailDeploymentReleaseNoteNotificationStrategy(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(NotificationSentEvent event) {
        SendDeploymentReleaseNoteNotificationEvent notificationEvent = (SendDeploymentReleaseNoteNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 프로젝트 릴리즈 노트 배포 안내입니다.";
        String url = "localhost:3000/projects/" + notificationEvent.getProjectId() + "/release-notes/" + notificationEvent.getReleaseNoteId();
        emailSender.sendEmail(
                getEmails(event),
                title,
                () -> EmailHtmlUtils.makeLinkHtml(
                        "릴리즈 노트 배포",
                        event.getTitle() + "릴리즈 노트가 배포되었습니다.",
                        "",
                        "아래 링크",
                        "에서 확인하실 수 있습니다.",
                        "링크",
                        url
                )
        );

    }

    private List<String> getEmails(NotificationSentEvent event) {
        return event.getTargets().stream().map(target -> target.getEmail()).collect(Collectors.toList());
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == SendDeploymentReleaseNoteNotificationEvent.class;
    }
}
