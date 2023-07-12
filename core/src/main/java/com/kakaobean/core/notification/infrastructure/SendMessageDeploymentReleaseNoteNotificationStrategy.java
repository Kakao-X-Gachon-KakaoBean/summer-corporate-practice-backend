package com.kakaobean.core.notification.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationStrategy;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import com.kakaobean.independentlysystem.amqp.DtoToQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.*;

@Slf4j
@Component
public class SendMessageDeploymentReleaseNoteNotificationStrategy implements SendMessageNotificationStrategy {

    private final AmqpService amqpService;
    private final ObjectMapper objectMapper;

    public SendMessageDeploymentReleaseNoteNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        this.amqpService = amqpService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(NotificationSentEvent event) {
        SendDeploymentReleaseNoteNotificationEvent notificationEvent = (SendDeploymentReleaseNoteNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 프로젝트 릴리즈 노트가 배포되었습니다.";

        //TODO 서버 배포하면 수정해야함.
        String url = "localhost:3000/projects/" + notificationEvent.getProjectId() + "/release-notes/" + notificationEvent.getReleaseNoteId();

        String exchangeName = PROJECT_PREFIX.getPrefix() + event.getProjectId();

        DtoToQueue dto = new DtoToQueue(title, LocalDateTime.now(), url);
        try {
            amqpService.send(exchangeName, "", objectMapper.writeValueAsString(dto));
        }
        catch(Exception e){
            log.error("메세지 큐 전송 중 에러가 발생했습니다.");
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == SendDeploymentReleaseNoteNotificationEvent.class;
    }

//    @PostConstruct
//    void test(){
//        String exchangeName = PROJECT_PREFIX.getPrefix() + 1L;
//        amqpService.send(exchangeName, "", new DtoToQueue("1", LocalDateTime.now(), "2"));
//    }
}
