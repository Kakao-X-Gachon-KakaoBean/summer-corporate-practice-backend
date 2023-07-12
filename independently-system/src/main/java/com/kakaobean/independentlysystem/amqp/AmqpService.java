package com.kakaobean.independentlysystem.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.springframework.amqp.core.Binding.DestinationType.*;

@Component
@RequiredArgsConstructor
public class AmqpService {

    private final AmqpAdmin amqpAdmin;
    private final RabbitTemplate rabbitTemplate;

    public void registerQueue(String queueName){
        Queue queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
    }

    public void registerDirectExchange(String exchangeName) {
        DirectExchange exchange = new DirectExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
    }

    public void registerFanOutExchange(String exchangeName) {
        FanoutExchange exchange = new FanoutExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
    }

    public void bindWithDirectExchange(String routingKey) {
        Binding binding = new Binding(routingKey, QUEUE, routingKey, routingKey, Collections.emptyMap());
        amqpAdmin.declareBinding(binding);
    }

    public void bindWithFanOutExchange(String exchangeName, String queueName) {
        Binding binding = new Binding(queueName, QUEUE, exchangeName, "", Collections.emptyMap());
        amqpAdmin.declareBinding(binding);
    }

    public void send(String exchangeName, String routingKey, String dto){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, dto);
    }
}
