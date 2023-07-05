package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.event.Event;

import lombok.Getter;

import java.util.List;

@Getter
public class ReleaseNoteRegisteredEvent extends Event {

    private final Long projectId;
    private final Long releaseNoteId;
    private final String releaseNoteTitle;
    private final List<NotifiedTargetInfo> notifiedInfos;

    public ReleaseNoteRegisteredEvent(Long projectId,
                                      Long releaseNoteId,
                                      String releaseNoteTitle,
                                      List<NotifiedTargetInfo> notifiedInfos) {
        this.projectId = projectId;
        this.releaseNoteId = releaseNoteId;
        this.releaseNoteTitle = releaseNoteTitle;
        this.notifiedInfos = notifiedInfos;
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
