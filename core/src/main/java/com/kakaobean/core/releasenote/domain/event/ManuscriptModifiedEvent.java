package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class ManuscriptModifiedEvent extends Event {

    private final Long manuscriptId;

    public ManuscriptModifiedEvent(Long manuscriptId) {
        this.manuscriptId = manuscriptId;
    }
}
