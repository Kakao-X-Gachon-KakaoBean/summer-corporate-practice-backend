package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.AssignmentTaskNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.USER_PREFIX;

@Slf4j
@Component
public class SendMessageAssignmentTaskNotificationStrategy extends AbstractSendMessageNotificationStrategy {

    public SendMessageAssignmentTaskNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        super(amqpService, objectMapper);
    }

    @Override
    public void send(NotificationSentEvent event) {
        AssignmentTaskNotificationEvent notificationEvent = (AssignmentTaskNotificationEvent) event;
        String exchangeName = USER_PREFIX.getPrefix() + notificationEvent.getWorkerId();
        super.sendWithDirect(notificationEvent.getUrl(), exchangeName, notificationEvent.getProjectTitle(), notificationEvent.getContent(), exchangeName);
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == AssignmentTaskNotificationEvent.class;
    }
}
