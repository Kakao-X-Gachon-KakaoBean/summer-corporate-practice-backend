package com.kakaobean.core.notification.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindNotificationsResponseDto {

    private List<FindNotificationResponseDto> notifications;

    public FindNotificationsResponseDto(List<FindNotificationResponseDto> notifications) {
        this.notifications = notifications;
    }
}
