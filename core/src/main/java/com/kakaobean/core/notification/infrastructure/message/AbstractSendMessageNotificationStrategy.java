package com.kakaobean.core.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationStrategy;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import com.kakaobean.independentlysystem.amqp.DtoToQueue;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class AbstractSendMessageNotificationStrategy implements SendMessageNotificationStrategy {

    private final AmqpService amqpService;
    private final ObjectMapper objectMapper;

    public AbstractSendMessageNotificationStrategy(AmqpService amqpService, ObjectMapper objectMapper) {
        this.amqpService = amqpService;
        this.objectMapper = objectMapper;
    }

    protected void sendWithFanout(String url,
                                  String exchangeName,
                                  String projectTitle,
                                  String title){
        DtoToQueue dto = new DtoToQueue(url, LocalDateTime.now(), projectTitle, title);
        try {
            amqpService.send(exchangeName, "", objectMapper.writeValueAsString(dto));
        }
        catch(Exception e){
            log.error("메세지 큐 전송 중 에러가 발생했습니다.");
            throw new RuntimeException(e.getCause());
        }
    }

    protected void sendWithDirect(String url,
                                  String exchangeName,
                                  String projectTitle,
                                  String title,
                                  String routingKey){
        DtoToQueue dto = new DtoToQueue(url, LocalDateTime.now(), projectTitle, title);
        try {
            amqpService.send(exchangeName, routingKey, objectMapper.writeValueAsString(dto));
        }
        catch(Exception e){
            log.error("메세지 큐 전송 중 에러가 발생했습니다.");
            throw new RuntimeException(e.getCause());
        }
    }
}
