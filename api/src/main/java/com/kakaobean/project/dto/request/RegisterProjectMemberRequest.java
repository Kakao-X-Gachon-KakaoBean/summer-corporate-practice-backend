package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import lombok.Getter;

@Getter
public class RegisterProjectMemberRequest {

    private String projectSecretKey;

    public RegisterProjectMemberRequest() {};


    public RegisterProjectMemberRequest(String projectSecretKey) {
        this.projectSecretKey = projectSecretKey;
    }

    public RegisterProjectMemberRequestDto toServiceDto(Long id) {
        return new RegisterProjectMemberRequestDto(projectSecretKey ,id);
    }
}
