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
    private LocalDate sprintStartDate;
    private LocalDate sprintDueDate;
    private List<TaskDto> tasks = new ArrayList<>();

    public FindSprintResponseDto(String sprintTitle, String sprintDesc, LocalDate sprintStartDate, LocalDate sprintDueDate) {
        this.sprintTitle = sprintTitle;
        this.sprintDesc = sprintDesc;
        this.sprintStartDate = sprintStartDate;
        this.sprintDueDate = sprintDueDate;
    }

    @Getter
    public static class TaskDto{
        private final String taskTitle;
        private final WorkStatus workerStatus;
        private final Long workerId;
        private final String workerName;
        private final String workerThumbnailImg;

        public TaskDto(String taskTitle, WorkStatus workerStatus, Long workerId, String workerName, String workerThumbnailImg) {
            this.taskTitle = taskTitle;
            this.workerId = workerId;
            this.workerName = workerName;
            this.workerStatus = workerStatus;
            this.workerThumbnailImg = workerThumbnailImg;
        }
    }


    // 테스트용
    public FindSprintResponseDto(String sprintTitle, String sprintDesc, LocalDate sprintStartDate, LocalDate sprintDueDate, List<TaskDto> tasks) {
        this.sprintTitle = sprintTitle;
        this.sprintDesc = sprintDesc;
        this.sprintStartDate = sprintStartDate;
        this.sprintDueDate = sprintDueDate;
        this.tasks = tasks;
    }
}
