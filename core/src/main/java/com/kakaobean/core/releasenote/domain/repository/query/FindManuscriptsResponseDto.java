package com.kakaobean.core.releasenote.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindManuscriptsResponseDto {

    private List<ManuscriptDto> manuscripts;

    public FindManuscriptsResponseDto(List<ManuscriptDto> manuscripts) {
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
