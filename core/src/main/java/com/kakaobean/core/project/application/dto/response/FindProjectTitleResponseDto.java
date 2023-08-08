package com.kakaobean.core.project.application.dto.response;

import lombok.Getter;

@Getter
public class
FindProjectTitleResponseDto {

    private final String projectTitle;

    public FindProjectTitleResponseDto(String projectTitle) {
        this.projectTitle = projectTitle;
    }
}
