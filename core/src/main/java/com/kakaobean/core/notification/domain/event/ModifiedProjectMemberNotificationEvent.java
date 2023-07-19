package com.kakaobean.core.notification.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ModifiedProjectMemberNotificationEvent extends NotificationSentEvent {

    private final String email;
    private final Long memberId;

    public ModifiedProjectMemberNotificationEvent(String url, String projectTitle, String content, LocalDateTime localDateTime, String email, Long memberId) {
        super(url, projectTitle, content, localDateTime);
        this.email = email;
        this.memberId = memberId;
    }
}
