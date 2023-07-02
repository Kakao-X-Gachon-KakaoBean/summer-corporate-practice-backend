package com.kakaobean.core.project.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindProjectResponseDto {

    private Long projectId;
    private String projectTitle;
    private String projectContent;

    public FindProjectResponseDto(Long projectId, String projectTitle, String projectContent) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
    }
}
