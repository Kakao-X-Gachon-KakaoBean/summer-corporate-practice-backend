package com.kakaobean.core.project.application.dto.request;

import lombok.Getter;

@Getter
public class RegisterProjectMemberRequestDto {

    private String projectSecretKey;
    private Long memberId;

    public RegisterProjectMemberRequestDto(String projectSecretKey, Long memberId) {
        this.projectSecretKey = projectSecretKey;
        this.memberId = memberId;
    }
}
