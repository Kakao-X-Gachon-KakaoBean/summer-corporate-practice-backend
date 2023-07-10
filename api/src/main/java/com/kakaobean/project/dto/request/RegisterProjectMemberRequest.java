package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class RegisterProjectMemberRequest {

    @NotEmpty
    private String projectSecretKey;

    public RegisterProjectMemberRequest(String projectSecretKey) {
        this.projectSecretKey = projectSecretKey;
    }

    public RegisterProjectMemberRequestDto toServiceDto(Long id) {
        return new RegisterProjectMemberRequestDto(projectSecretKey ,id);
    }
}
