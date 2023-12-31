package com.kakaobean.core.notification.domain.event.handler.member;

import com.kakaobean.core.member.domain.event.MemberRegisteredEvent;
import com.kakaobean.core.notification.domain.service.register.RegisterMessageQueueService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RegisterMemberQueueWithMemberRegisteredEventHandler {

    private final RegisterMessageQueueService registerMessageQueueService;

    /**
     * 멤버 개인 큐를 생성한다.
     */
    @TransactionalEventListener(MemberRegisteredEvent.class)
    public void handle(MemberRegisteredEvent event){
        if(event != null){
            registerMessageQueueService.registerPersonalQueue(event.getMemberId());
        }
    }
}
