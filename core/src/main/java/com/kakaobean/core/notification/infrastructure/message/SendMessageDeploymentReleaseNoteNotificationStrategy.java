package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.*;

@Slf4j
@Component
public class SendMessageDeploymentReleaseNoteNotificationStrategy extends AbstractSendMessageNotificationStrategy {

    public SendMessageDeploymentReleaseNoteNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        super(amqpService, objectMapper);
    }

    @Override
    public void send(NotificationSentEvent event) {
        SendDeploymentReleaseNoteNotificationEvent notificationEvent = (SendDeploymentReleaseNoteNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 릴리즈 노트가 배포되었습니다.";

        //TODO 서버 배포하면 수정해야함.
        String url = "localhost:3000" + event.getUrl();
        String exchangeName = PROJECT_PREFIX.getPrefix() + notificationEvent.getProjectId();
        super.sendWithFanout(url, exchangeName, notificationEvent.getProjectTitle(), title);
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == SendDeploymentReleaseNoteNotificationEvent.class;
    }
}
