package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.event.Event;

import lombok.Getter;

import java.util.List;

@Getter
public class ReleaseNoteRegisteredEvent extends Event {

    private final Long releaseNoteId;

    public ReleaseNoteRegisteredEvent(Long releaseNoteId) {
        this.releaseNoteId = releaseNoteId;
    }
}
