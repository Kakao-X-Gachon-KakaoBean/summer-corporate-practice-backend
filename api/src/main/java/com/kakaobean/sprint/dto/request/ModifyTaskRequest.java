package com.kakaobean.sprint.dto.request;

import com.kakaobean.core.sprint.application.dto.ModifyTaskRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ModifyTaskRequest {

    @NotEmpty
    private String taskTitle;

    @NotEmpty
    private String taskDesc;

    @NotNull
    private Long sprintId;

    public ModifyTaskRequest(String taskTitle, String taskDesc, Long sprintId) {
        this.taskTitle = taskTitle;
        this.taskDesc = taskDesc;
        this.sprintId = sprintId;
    }

    public ModifyTaskRequestDto toServiceDto(Long adminId, Long taskId){
        return new ModifyTaskRequestDto(
                taskId,
                taskTitle,
                taskDesc,
                sprintId,
                adminId
        );
    }
}
