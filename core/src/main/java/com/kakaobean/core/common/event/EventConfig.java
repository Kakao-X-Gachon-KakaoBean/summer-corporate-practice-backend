package com.kakaobean.core.common.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class EventConfig {

    private final ApplicationContext applicationContext;

    @PostConstruct
    public void eventsInitializer(){
        Events.setPublisher(applicationContext);
    }
}
