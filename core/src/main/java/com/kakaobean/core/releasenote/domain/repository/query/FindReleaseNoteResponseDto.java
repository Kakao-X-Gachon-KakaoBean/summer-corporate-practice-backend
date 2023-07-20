package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindReleaseNoteResponseDto {

    private final Long releaseNoteId;
    private final String releaseNoteTitle;
    private final String releaseNoteContent;
    private final String releaseNoteVersion;
    private final String createdAt;

    public FindReleaseNoteResponseDto(Long releaseNoteId, String releaseNoteTitle, String releaseNoteContent, String releaseNoteVersion, String createdAt) {
        this.releaseNoteId = releaseNoteId;
        this.releaseNoteTitle = releaseNoteTitle;
        this.releaseNoteContent = releaseNoteContent;
        this.releaseNoteVersion = releaseNoteVersion;
        this.createdAt = createdAt;
    }
}
