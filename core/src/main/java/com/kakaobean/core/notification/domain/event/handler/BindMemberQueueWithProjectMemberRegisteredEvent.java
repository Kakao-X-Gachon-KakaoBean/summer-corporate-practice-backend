package com.kakaobean.core.notification.domain.event.handler;

import com.kakaobean.core.notification.domain.service.register.RegisterMessageQueueService;
import com.kakaobean.core.project.domain.event.ProjectMemberRegisteredEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BindMemberQueueWithProjectMemberRegisteredEvent {

    private final RegisterMessageQueueService registerMessageQueueService;

    @TransactionalEventListener(ProjectMemberRegisteredEvent.class)
    public void handle(ProjectMemberRegisteredEvent event) {
        if(event != null){
            registerMessageQueueService.bindProjectExchangeAndPersonalQueue(event.getProjectId(), event.getMemberId());
        }
    }
}
