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

    public RegisterTaskRequest(String title, String content, Long sprintId) {
        this.title = title;
        this.content = content;
        this.sprintId = sprintId;
    }

    public RegisterTaskRequestDto toServiceDto(Long adminId) {
        return new RegisterTaskRequestDto(
                title,
                content,
                sprintId,
                adminId
        );
    }
}