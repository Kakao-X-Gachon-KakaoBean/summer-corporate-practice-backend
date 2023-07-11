package com.kakaobean.core.notification.domain.service.register;

public interface RegisterMessageQueueService {
    void registerPersonalQueue(Long memberId);
    void registerProjectExchange(Long projectId);
    void bindProjectExchangeAndPersonalQueue(Long projectId, Long memberId);
}
