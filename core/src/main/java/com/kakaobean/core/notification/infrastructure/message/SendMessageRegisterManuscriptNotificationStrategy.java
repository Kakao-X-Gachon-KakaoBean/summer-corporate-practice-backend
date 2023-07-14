package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.SendRegisterManuscriptNotificationEvent;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationStrategy;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import com.kakaobean.independentlysystem.amqp.DtoToQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.PROJECT_PREFIX;

@Component
@Slf4j
public class SendMessageRegisterManuscriptNotificationStrategy extends AbstractSendMessageNotificationStrategy {

    public SendMessageRegisterManuscriptNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        super(amqpService, objectMapper);
    }

    @Override
    public void send(NotificationSentEvent event) {
        SendRegisterManuscriptNotificationEvent notificationEvent = (SendRegisterManuscriptNotificationEvent) event;

        //TODO 서버 배포하면 수정해야함
        String url = "localhost:3000" + notificationEvent.getUrl();
        String exchangeName = PROJECT_PREFIX.getPrefix() + notificationEvent.getProjectId();

        super.sendWithFanout(url, exchangeName, notificationEvent.getProjectTitle(), notificationEvent.getContent());
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == SendRegisterManuscriptNotificationEvent.class;
    }
}
