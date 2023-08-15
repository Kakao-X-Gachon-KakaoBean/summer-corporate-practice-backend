package com.kakaobean.core.notification.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindPagingNotificationResponseDto {

    private final boolean finalPage;
    private final List<FindNotificationResponseDto> notifications;

    public FindPagingNotificationResponseDto(boolean finalPage, List<FindNotificationResponseDto> notifications) {
        this.finalPage = finalPage;
        this.notifications = notifications;
    }

}
