package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindPagingReleaseNotesResponseDto {

    private final boolean finalPage;
    private final List<ReleaseNoteDto> releaseNotes;

    public FindPagingReleaseNotesResponseDto(boolean finalPage, List<ReleaseNoteDto> releaseNotes) {
        this.finalPage = finalPage;
        this.releaseNotes = releaseNotes;
    }

    @Getter
    public static class ReleaseNoteDto {

        private final Long id;
        private final String title;
        private final String version;
        private final String content;
        private final String createdAt;

        public ReleaseNoteDto(Long id, String title, String version, String content, String createdAt) {
            this.id = id;
            this.title = title;
            this.version = version;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
