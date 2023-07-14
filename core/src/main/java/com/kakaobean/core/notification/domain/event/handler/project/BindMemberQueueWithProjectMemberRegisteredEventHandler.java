package com.kakaobean.core.notification.domain.event.handler.project;

import com.kakaobean.core.notification.domain.service.register.RegisterMessageQueueService;
import com.kakaobean.core.project.domain.event.ProjectMemberRegisteredEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BindMemberQueueWithProjectMemberRegisteredEventHandler {

    private final RegisterMessageQueueService registerMessageQueueService;

    /**
     * 프로젝트에 멤버가 참여하면 프로젝트 Fan Out Exchange와 멤버 개인 큐를 바인딩한다.
     */
    @TransactionalEventListener(ProjectMemberRegisteredEvent.class)
    public void handle(ProjectMemberRegisteredEvent event) {
        if(event != null){
            registerMessageQueueService.bindProjectExchangeAndPersonalQueue(event.getProjectId(), event.getMemberId());
        }
    }
}
