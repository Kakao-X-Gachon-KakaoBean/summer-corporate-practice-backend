package com.kakaobean.core.sprint.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SprintsDto {
    private Long sprintId;
    private String sprintTitle;
    private LocalDate startDate;
    private LocalDate dueDate;
    private List<TasksDto> children = new ArrayList<>();

    public SprintsDto(Long sprintId, String sprintTitle, LocalDate startDate, LocalDate dueDate) {
        this.sprintId = sprintId;
        this.sprintTitle = sprintTitle;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    // 테스트용
    public SprintsDto(Long sprintId, String sprintTitle, LocalDate startDate, LocalDate dueDate, List<TasksDto> children) {
        this.sprintId = sprintId;
        this.sprintTitle = sprintTitle;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.children = children;
    }
}
