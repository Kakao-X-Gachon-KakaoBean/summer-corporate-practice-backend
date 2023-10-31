package com.kakaobean.core.project.application.dto.response;

import lombok.Getter;

@Getter
public class RegisterProjectResponseDto {

    private final Long projectId;

    public RegisterProjectResponseDto(Long projectId) {
        this.projectId = projectId;
    }
}
