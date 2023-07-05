package com.kakaobean.core.notification.application.dto;

import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRegisteredEvent;
import lombok.Getter;

import java.util.List;

import static com.kakaobean.core.notification.domain.NotificationType.*;
import static java.util.stream.Collectors.*;

@Getter
public class RegisterNotificationRequestDto {

    private final Long projectId;
    private final Long sourceId;
    private final String title;
    private final List<NotifiedTargetInfo> notifiedInfos;
    private final NotificationType type;

    public RegisterNotificationRequestDto(Long projectId,
                                          Long sourceId,
                                          String title,
                                          List<NotifiedTargetInfo> notifiedInfos,
                                          NotificationType type) {
        this.projectId = projectId;
        this.sourceId = sourceId;
        this.title = title;
        this.notifiedInfos = notifiedInfos;
        this.type = type;
    }

    public static RegisterNotificationRequestDto from(ReleaseNoteRegisteredEvent event) {
        return new RegisterNotificationRequestDto(
                event.getProjectId(),
                event.getReleaseNoteId(),
                event.getReleaseNoteTitle(),
                event.getNotifiedInfos()
                        .stream()
                        .map(info -> new NotifiedTargetInfo(info.getMemberId(), info.getNotifiedEmails()))
                        .collect(toList()),
                RELEASE_NOTE
        );
    }

    @Getter
    public static class NotifiedTargetInfo {

        private final Long memberId;
        private final String notifiedEmails;

        public NotifiedTargetInfo(Long memberId, String notifiedEmails) {
            this.memberId = memberId;
            this.notifiedEmails = notifiedEmails;
        }
    }
}
