package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;

import lombok.Getter;

@Getter
public class ReleaseNoteDeployedEvent extends Event {

    private final Long releaseNoteId;

    public ReleaseNoteDeployedEvent(Long releaseNoteId) {
        this.releaseNoteId = releaseNoteId;
    }
}
