package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.StartManuscriptModificationNotificationEvent;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.PROJECT_PREFIX;

@Slf4j
@Component
public class SendMessageManuscriptModificationStartNotificationStrategy extends AbstractSendMessageNotificationStrategy {

    public SendMessageManuscriptModificationStartNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        super(amqpService, objectMapper);
    }

    @Override
    public void send(NotificationSentEvent event) {
        StartManuscriptModificationNotificationEvent notificationEvent = (StartManuscriptModificationNotificationEvent) event;
        String exchangeName = PROJECT_PREFIX.getPrefix() + notificationEvent.getProjectId();
        super.sendWithDirect(notificationEvent.getUrl(), exchangeName, notificationEvent.getProjectTitle(), notificationEvent.getContent(), exchangeName);
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == StartManuscriptModificationNotificationEvent.class;
    }
}
