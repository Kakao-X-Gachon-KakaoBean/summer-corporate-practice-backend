package com.kakaobean.core.sprint.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyTaskRequestDto {

    private Long taskId;
    private String newTitle;
    private String newContent;
    private Long sprintId;
    private Long adminId;

    public ModifyTaskRequestDto(Long taskId, String newTitle, String newContent, Long sprintId, Long adminId) {
        this.taskId = taskId;
        this.newTitle = newTitle;
        this.newContent = newContent;
        this.sprintId = sprintId;
        this.adminId = adminId;
    }
}
