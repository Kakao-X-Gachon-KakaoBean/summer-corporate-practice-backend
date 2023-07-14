package com.kakaobean.core.notification.domain.event.handler.project;

import com.kakaobean.core.notification.domain.service.register.RegisterMessageQueueService;
import com.kakaobean.core.project.domain.event.ProjectRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProjectRegisteredEventHandler {

    private final RegisterMessageQueueService registerMessageQueueService;

    /**
     * 프로젝트가 생성되면 실행되는 메서드
     *
     * 프로젝트 팬아웃 익스체인지를 생성한다.
     * 프로젝트 팬아웃 익스체인지와 관리자의 큐를 바인딩한다.
     */
    @TransactionalEventListener(ProjectRegisteredEvent.class)
    public void registerProjectExchangeAndBindWithAdminQueue(ProjectRegisteredEvent event){
        if(event != null){
            registerMessageQueueService.registerProjectExchange(event.getProjectId());
            registerMessageQueueService.bindProjectExchangeAndPersonalQueue(event.getProjectId(), event.getProjectAdminId());
        }
    }
}
