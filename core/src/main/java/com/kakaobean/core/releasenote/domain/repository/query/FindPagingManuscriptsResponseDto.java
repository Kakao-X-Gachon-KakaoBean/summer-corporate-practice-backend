package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindPagingManuscriptsResponseDto {

    private final boolean finalPage;
    private final List<ManuscriptDto> manuscripts;

    public FindPagingManuscriptsResponseDto(boolean finalPage, List<ManuscriptDto> manuscripts) {
        this.finalPage = finalPage;
        this.manuscripts = manuscripts;
    }

    @Getter
    public static class ManuscriptDto {
        private final Long id;
        private final String title;
        private final String version;

        public ManuscriptDto(Long id, String title, String version) {
            this.id = id;
            this.title = title;
            this.version = version;
        }
    }
}
