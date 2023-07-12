package com.kakaobean.core.notification.infrastructure;

import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationStrategy;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import com.kakaobean.independentlysystem.amqp.DtoToQueue;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobean.core.notification.infrastructure.QueueNameConfig.*;

@Component
public class SendMessageDeploymentReleaseNoteNotificationStrategy implements SendMessageNotificationStrategy {

    private final AmqpService amqpService;

    public SendMessageDeploymentReleaseNoteNotificationStrategy(AmqpService amqpService) {
        this.amqpService = amqpService;
    }

    @Override
    public void send(NotificationSentEvent event) {
        SendDeploymentReleaseNoteNotificationEvent notificationEvent = (SendDeploymentReleaseNoteNotificationEvent) event;
        String title = notificationEvent.getProjectTitle() + " 프로젝트 릴리즈 노트가 배포되었습니다.";

        //TODO 서버 배포하면 수정해야함.
        String url = "localhost:3000/projects/" + notificationEvent.getProjectId() + "/release-notes/" + notificationEvent.getReleaseNoteId();

        String exchangeName = PROJECT_PREFIX.getPrefix() + event.getProjectId();
        amqpService.send(exchangeName, "", new DtoToQueue(title, LocalDateTime.now(), url));
    }

    @Override
    public boolean support(Class<? extends NotificationSentEvent> eventClass) {
        return eventClass == SendDeploymentReleaseNoteNotificationEvent.class;
    }
}
