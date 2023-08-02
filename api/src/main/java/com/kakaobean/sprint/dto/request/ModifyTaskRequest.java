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
    private String newTitle;

    @NotEmpty
    private String newContent;

    @NotNull
    private Long sprintId;

    public ModifyTaskRequest(String newTitle, String newContent, Long sprintId) {
        this.newTitle = newTitle;
        this.newContent = newContent;
        this.sprintId = sprintId;
    }

    public ModifyTaskRequestDto toServiceDto(Long adminId, Long taskId){
        return new ModifyTaskRequestDto(
                taskId,
                newTitle,
                newContent,
                sprintId,
                adminId
        );
    }
}
