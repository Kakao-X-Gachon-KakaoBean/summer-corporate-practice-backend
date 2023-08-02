package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.DeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.ModifiedProjectMemberNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.infrastructure.QueueNameConfig;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.*;

@Slf4j
@Component
public class SendMessageModifiedProjectMemberRoleNotificationStrategy extends AbstractSendMessageNotificationStrategy {

    public SendMessageModifiedProjectMemberRoleNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        super(amqpService, objectMapper);
    }

    @Override
    public void send(NotificationSentEvent event) {
        ModifiedProjectMemberNotificationEvent notificationEvent = (ModifiedProjectMemberNotificationEvent) event;
        String exchangeName = USER_PREFIX.getPrefix() + notificationEvent.getMemberId();
        super.sendWithDirect(notificationEvent.getUrl(), exchangeName, notificationEvent.getProjectTitle(), notificationEvent.getContent(), exchangeName);
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == ModifiedProjectMemberNotificationEvent.class;
    }
}
