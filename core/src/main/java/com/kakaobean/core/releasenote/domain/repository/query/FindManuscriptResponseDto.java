package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;

@Getter
public class FindManuscriptResponseDto {

    private final String lastEditedMemberName;
    private final Long manuscriptId;
    private final String manuscriptTitle;
    private final String manuscriptContent;
    private final String manuscriptVersion;

    public FindManuscriptResponseDto(String lastEditedMemberName,
                                     Long manuscriptId,
                                     String manuscriptTitle,
                                     String manuscriptContent,
                                     String manuscriptVersion) {
        this.lastEditedMemberName = lastEditedMemberName;
        this.manuscriptId = manuscriptId;
        this.manuscriptTitle = manuscriptTitle;
        this.manuscriptContent = manuscriptContent;
        this.manuscriptVersion = manuscriptVersion;
    }
}
