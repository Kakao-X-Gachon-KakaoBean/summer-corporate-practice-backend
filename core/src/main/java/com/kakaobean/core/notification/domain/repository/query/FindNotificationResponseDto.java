package com.kakaobean.core.notification.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindNotificationResponseDto {

    private Long notificationId;
    private String createdAt;
    private String content;
    private String url;
    private boolean hasRead;

    public FindNotificationResponseDto(Long notificationId, String createdAt, String content, String url, boolean hasRead) {
        this.notificationId = notificationId;
        this.createdAt = createdAt;
        this.content = content;
        this.url = url;
        this.hasRead = hasRead;
    }
}
