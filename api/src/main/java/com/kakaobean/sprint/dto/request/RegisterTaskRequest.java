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
    private String taskDesc;

    @NotNull
    private Long sprintId;

    public RegisterTaskRequest(String taskTitle, String taskDesc, Long sprintId) {
        this.taskTitle = taskTitle;
        this.taskDesc = taskDesc;
        this.sprintId = sprintId;
    }

    public RegisterTaskRequestDto toServiceDto(Long adminId) {
        return new RegisterTaskRequestDto(
                taskTitle,
                taskDesc,
                sprintId,
                adminId
        );
    }
}