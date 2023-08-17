package com.kakaobean.core.project.domain.repository.query;

import lombok.Getter;

@Getter
public class
FindProjectTitleResponseDto {

    private final String projectTitle;

    public FindProjectTitleResponseDto(String projectTitle) {
        this.projectTitle = projectTitle;
    }
}
