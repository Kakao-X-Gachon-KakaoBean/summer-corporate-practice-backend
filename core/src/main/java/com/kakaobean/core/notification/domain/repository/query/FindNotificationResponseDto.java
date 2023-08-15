package com.kakaobean.core.notification.domain.repository.query;

import lombok.Getter;

@Getter
public class FindNotificationResponseDto {

    private String createdAt;
    private String content;
    private String url;
    private boolean hasRead;

    public FindNotificationResponseDto(String createdAt, String content, String url, boolean hasRead) {
        this.createdAt = createdAt;
        this.content = content;
        this.url = url;
        this.hasRead = hasRead;
    }
}
