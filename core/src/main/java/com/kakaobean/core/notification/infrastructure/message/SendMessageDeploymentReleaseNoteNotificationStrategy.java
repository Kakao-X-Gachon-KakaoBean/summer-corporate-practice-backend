package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.DeploymentReleaseNoteNotificationEvent;
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
        DeploymentReleaseNoteNotificationEvent notificationEvent = (DeploymentReleaseNoteNotificationEvent) event;
        String exchangeName = PROJECT_PREFIX.getPrefix() + notificationEvent.getProjectId();
        super.sendWithFanout(notificationEvent.getUrl(), exchangeName, notificationEvent.getProjectTitle(), notificationEvent.getContent());
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == DeploymentReleaseNoteNotificationEvent.class;
    }
}
