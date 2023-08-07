package com.kakaobean.core.releasenote.domain.repository.query;

import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptStatus;
import lombok.Getter;

@Getter
public class FindManuscriptResponseDto {

    private final String lastEditedMemberName;
    private final Long manuscriptId;
    private final String manuscriptTitle;
    private final String manuscriptContent;
    private final String manuscriptVersion;
    private final String createdAt;
    private final ManuscriptStatus manuscriptStatus;

    public FindManuscriptResponseDto(String lastEditedMemberName,
                                     Long manuscriptId,
                                     String manuscriptTitle,
                                     String manuscriptContent,
                                     String manuscriptVersion,
                                     String createdAt,
                                     ManuscriptStatus manuscriptStatus) {
        this.lastEditedMemberName = lastEditedMemberName;
        this.manuscriptId = manuscriptId;
        this.manuscriptTitle = manuscriptTitle;
        this.manuscriptContent = manuscriptContent;
        this.manuscriptVersion = manuscriptVersion;
        this.createdAt = createdAt;
        this.manuscriptStatus = manuscriptStatus;
    }
}
