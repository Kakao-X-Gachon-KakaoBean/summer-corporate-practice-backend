package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class ManuscriptRegisteredEvent extends Event {

    private final Long projectId;
    private final Long manuscriptId;
    private final String title;

    public ManuscriptRegisteredEvent(Long projectId, Long manuscriptId, String title) {
        this.projectId = projectId;
        this.manuscriptId = manuscriptId;
        this.title = title;
    }
}
