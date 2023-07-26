package com.kakaobean.core.sprint.application.dto;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterTaskRequestDto {

    private String title;
    private String content;
    private Long sprintId;
    private Long projectId;
    private Long adminId;

    public RegisterTaskRequestDto(String title, String content, Long sprintId, Long projectId, Long adminId) {
        this.title = title;
        this.content = content;
        this.sprintId = sprintId;
        this.projectId = projectId;
        this.adminId = adminId;
    }

    public Task toEntity(){
        return new Task(
                BaseStatus.ACTIVE,
                null,
                sprintId,
                title,
                content,
                WorkStatus.NOT_ASSIGNED
        );
    }
}
