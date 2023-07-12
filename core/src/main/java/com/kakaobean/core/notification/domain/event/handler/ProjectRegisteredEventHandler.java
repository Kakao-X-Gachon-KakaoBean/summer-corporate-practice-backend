package com.kakaobean.core.notification.domain.event.handler;

import com.kakaobean.core.notification.domain.service.register.RegisterMessageQueueService;
import com.kakaobean.core.project.domain.event.ProjectRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProjectRegisteredEventHandler {

    private final RegisterMessageQueueService registerMessageQueueService;

    @TransactionalEventListener(ProjectRegisteredEvent.class)
    public void registerProjectExchangeAndBindWithAdminQueue(ProjectRegisteredEvent event){
        if(event != null){
            registerMessageQueueService.registerProjectExchange(event.getProjectId());
            registerMessageQueueService.bindProjectExchangeAndPersonalQueue(event.getProjectId(), event.getProjectAdminId());
        }
    }
}
