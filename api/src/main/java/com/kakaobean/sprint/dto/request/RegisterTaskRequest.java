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
    private String taskTitle;

    @NotEmpty
    private String taskContent;

    @NotNull
    private Long sprintId;

    public RegisterTaskRequest(String taskTitle, String taskContent, Long sprintId) {
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.sprintId = sprintId;
    }

    public RegisterTaskRequestDto toServiceDto(Long adminId) {
        return new RegisterTaskRequestDto(
                taskTitle,
                taskContent,
                sprintId,
                adminId
        );
    }
}