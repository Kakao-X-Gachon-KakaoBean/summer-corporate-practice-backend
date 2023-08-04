package com.kakaobean.core.sprint.domain.repository.query;

import lombok.Getter;

@Getter
public class TasksDto {
    private final Long taskId;
    private final String taskTitle;

    public TasksDto(Long taskId,
                    String taskTitle) {

        this.taskId = taskId;
        this.taskTitle = taskTitle;
    }
}
