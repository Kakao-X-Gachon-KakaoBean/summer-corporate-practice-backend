package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.common.event.Event;

import lombok.Getter;

@Getter
public class ReleaseNoteDeployedEvent extends Event {

    private final Long releaseNoteId;
    private final String version;

    public ReleaseNoteDeployedEvent(Long releaseNoteId, String version) {
        this.releaseNoteId = releaseNoteId;
        this.version = version;
    }
}
