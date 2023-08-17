package com.kakaobean.core.project.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindProjectsResponseDto {

    private List<FindProjectResponseDto> projects;

    public FindProjectsResponseDto(List<FindProjectResponseDto> projects) {
        this.projects = projects;
    }
}
