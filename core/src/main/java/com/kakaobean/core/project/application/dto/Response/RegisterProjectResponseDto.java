package com.kakaobean.core.project.application.dto.Response;

import lombok.Getter;

@Getter
public class RegisterProjectResponseDto {

    private final Long ProjectId;

    public RegisterProjectResponseDto(Long projectId) {
        ProjectId = projectId;
    }
}
