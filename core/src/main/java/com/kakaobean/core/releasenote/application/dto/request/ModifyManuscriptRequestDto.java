package com.kakaobean.core.releasenote.application.dto.request;

import lombok.Getter;

@Getter
public class ModifyManuscriptRequestDto {

    private final String title;
    private final String content;
    private final String version;
    private final Long editingMemberId;
    private final Long manuscriptId;

    public ModifyManuscriptRequestDto(String title, String content, String version, Long editingMemberId, Long manuscriptId) {
        this.title = title;
        this.content = content;
        this.version = version;
        this.editingMemberId = editingMemberId;
        this.manuscriptId = manuscriptId;
    }
}
