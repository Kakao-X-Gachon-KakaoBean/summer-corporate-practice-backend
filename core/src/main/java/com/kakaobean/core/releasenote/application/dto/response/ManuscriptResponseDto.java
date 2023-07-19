package com.kakaobean.core.releasenote.application.dto.response;

import lombok.Getter;

@Getter
public class ManuscriptResponseDto {

    private final Long manuscriptId;
    private final String manuscriptTitle;
    private final String manuscriptContent;
    private final String manuscriptVersion;

    public ManuscriptResponseDto(Long manuscriptId, String manuscriptTitle, String manuscriptContent, String manuscriptVersion) {
        this.manuscriptId = manuscriptId;
        this.manuscriptTitle = manuscriptTitle;
        this.manuscriptContent = manuscriptContent;
        this.manuscriptVersion = manuscriptVersion;
    }
}
