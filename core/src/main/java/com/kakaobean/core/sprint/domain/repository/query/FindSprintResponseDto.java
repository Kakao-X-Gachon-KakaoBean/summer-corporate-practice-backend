package com.kakaobean.core.sprint.domain.repository.query;

import com.kakaobean.core.sprint.domain.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FindSprintResponseDto {

    private String sprintTitle;
    private String sprintDesc;
    private LocalDate startDate;
    private LocalDate dueDate;
    private List<TaskDto> children = new ArrayList<>();

    public FindSprintResponseDto(String sprintTitle, String sprintDesc, LocalDate startDate, LocalDate dueDate) {
        this.sprintTitle = sprintTitle;
        this.sprintDesc = sprintDesc;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    @Getter
    public static class TaskDto{
        private final Long taskId;
        private final String taskTitle;
        private final WorkStatus workStatus;
        private final Long workerId;
        private final String workerName;
        private final String workerThumbnailImg;

        public TaskDto(Long taskId, String taskTitle, WorkStatus workStatus, Long workerId, String workerName, String workerThumbnailImg) {
            this.taskId = taskId;
            this.taskTitle = taskTitle;
            this.workerId = workerId;
            this.workerName = workerName;
            this.workStatus = workStatus;
            this.workerThumbnailImg = workerThumbnailImg;
        }
    }


    // 테스트용
    public FindSprintResponseDto(String sprintTitle, String sprintDesc, LocalDate startDate, LocalDate dueDate, List<TaskDto> children) {
        this.sprintTitle = sprintTitle;
        this.sprintDesc = sprintDesc;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.children = children;
    }
}
