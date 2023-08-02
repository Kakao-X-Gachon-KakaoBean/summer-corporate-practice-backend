package com.kakaobean.core.sprint.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class SprintsDto {
    private final Long sprintId;
    private final String sprintTitle;
    private final String startDate;
    private final String dueDate;
    private final List<TasksDto> children;

    public SprintsDto(Long sprintId,
                      String sprintTitle,
                      String startDate,
                      String dueDate,
                      List<TasksDto> children) {

        this.sprintId = sprintId;
        this.sprintTitle = sprintTitle;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.children = children;
    }
}
