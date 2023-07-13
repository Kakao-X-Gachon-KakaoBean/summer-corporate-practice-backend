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
        String title = notificationEvent.getTargetTitle() + " 원고가 등록되었습니다.";

        //TODO 서버 배포하면 수정해야함
        String url = "localhost:3000/projects/" + notificationEvent.getProjectId() + "/manuscripts/" + notificationEvent.getTargetId();
        String exchangeName = PROJECT_PREFIX.getPrefix() + event.getProjectId();

        super.sendWithFanout(url, exchangeName, notificationEvent.getProjectTitle(), title);
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == SendRegisterManuscriptNotificationEvent.class;
    }
}
