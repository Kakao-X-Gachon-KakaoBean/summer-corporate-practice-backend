package com.kakaobean.sprint.dto.request;

import com.kakaobean.core.sprint.application.dto.RegisterTaskRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RegisterTaskRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    private Long sprintId;

    @NotNull
    private Long projectId;

    public RegisterTaskRequest(String title, String content, Long sprintId, Long projectId) {
        this.title = title;
        this.content = content;
        this.sprintId = sprintId;
        this.projectId = projectId;
    }

    public RegisterTaskRequestDto toService(Long adminId) {
        return new RegisterTaskRequestDto(
                title,
                content,
                sprintId,
                projectId,
                adminId
        );
    }
}