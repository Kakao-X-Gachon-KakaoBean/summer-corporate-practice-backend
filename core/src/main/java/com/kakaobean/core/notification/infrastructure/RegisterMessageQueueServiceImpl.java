package com.kakaobean.core.notification.infrastructure;

import com.kakaobean.core.notification.domain.service.register.RegisterMessageQueueService;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterMessageQueueServiceImpl implements RegisterMessageQueueService {

    private final AmqpService amqpService;

    private static final String USER_PREFIX = "user:";
    private static final String PROJECT_PREFIX = "project:";

    @Override
    public void registerPersonalQueue(Long memberId) {
        String routingKey = USER_PREFIX + memberId;

        //멤버 개인 키 등록
        amqpService.registerQueue(routingKey);

        //멤버 개인 Direct Exchange 등록
        amqpService.registerDirectExchange(routingKey);

        //exchange와 queue를 바인딩
        amqpService.bindWithDirectExchange(routingKey);
    }

    @Override
    public void registerProjectExchange(Long projectId) {
        String exchangeName = PROJECT_PREFIX + projectId;

        //프로젝트의 fanout exchange 등록
        amqpService.registerFanOutExchange(exchangeName);
    }

    @Override
    public void bindProjectExchangeAndPersonalQueue(Long projectId, Long memberId) {
        String exchangeName = PROJECT_PREFIX + projectId;
        String queueName = USER_PREFIX + memberId;

        //프로젝트의 fanout exchange, 개인 큐 바인딩
        amqpService.bindWithFanOutExchange(exchangeName, queueName);
    }
}
