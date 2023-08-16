package com.kakaobean.core.notification.infrastructure.email;

import com.kakaobean.common.EmailHtmlUtils;
import com.kakaobean.core.notification.config.EmailProperties;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.RegisterCommentNotificationEvent;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationStrategy;
import com.kakaobean.independentlysystem.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendEmailPostedCommentNotificationStrategy implements SendEmailNotificationStrategy {

    private final EmailSender emailSender;
    private final EmailProperties emailProperties;

    @Override
    public void send(NotificationSentEvent event) {
        RegisterCommentNotificationEvent notificationEvent = (RegisterCommentNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + "작성하신 이슈에 댓글이 달렸습니다.";
        String url = emailProperties.getHostName() + notificationEvent.getUrl();
        emailSender.sendEmail(
                List.of(notificationEvent.getEmail()),
                title,
                () -> EmailHtmlUtils.makeLinkHtml(
                        "이슈 댓글 알림",
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
        return eventClass == RegisterCommentNotificationEvent.class;
    }
}
