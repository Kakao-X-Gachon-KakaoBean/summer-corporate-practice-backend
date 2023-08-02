package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class ManuscriptModificationStartedEvent extends Event {

    private final Long manuscriptId;

    public ManuscriptModificationStartedEvent(Long manuscriptId) {
        this.manuscriptId = manuscriptId;
    }
}
