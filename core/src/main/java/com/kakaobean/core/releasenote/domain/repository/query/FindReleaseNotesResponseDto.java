package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindReleaseNotesResponseDto {

    private final List<FindReleaseNotesResponseDto.ReleaseNoteDto> releaseNotes;

    public FindReleaseNotesResponseDto(List<FindReleaseNotesResponseDto.ReleaseNoteDto> releaseNotes) {
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
