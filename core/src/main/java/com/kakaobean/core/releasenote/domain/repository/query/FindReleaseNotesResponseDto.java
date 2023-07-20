package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindReleaseNotesResponseDto {

    private final boolean finalPage;
    private final List<ReleaseNoteDto> releaseNotes;

    public FindReleaseNotesResponseDto(boolean finalPage, List<ReleaseNoteDto> releaseNotes) {
        this.finalPage = finalPage;
        this.releaseNotes = releaseNotes;
    }

    @Getter
    public static class ReleaseNoteDto {

        private final Long id;
        private final String title;
        private final String version;

        public ReleaseNoteDto(Long id, String title, String version) {
            this.id = id;
            this.title = title;
            this.version = version;
        }
    }
}
