package com.kakaobean.core.notification.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class NotificationSentEvent extends Event {

    private final Long projectId;
    private final String title;
    private final List<NotificationTarget> targets;
    private final String projectTitle;

    public NotificationSentEvent(Long projectId,
                                 String title,
                                 List<NotificationTarget> targets,
                                 String projectTitle) {
        this.projectId = projectId;
        this.title = title;
        this.targets = targets;
        this.projectTitle = projectTitle;
    }

    @Getter
    public static class NotificationTarget {

        private final String email;
        private final Long memberId;

        public NotificationTarget(String email, Long memberId) {
            this.email = email;
            this.memberId = memberId;
        }
    }
}
