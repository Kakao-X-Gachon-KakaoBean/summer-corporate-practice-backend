package com.kakaobean.core.sprint.domain.repository.query;

import com.kakaobean.core.sprint.domain.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindTaskResponseDto {

    private String taskTitle;
    private String taskDesc;
    private WorkStatus workStatus;
    private Long workerId;
    private String workerName;
    private String workerThumbnailImg;
    private Long sprintId;

    public FindTaskResponseDto(String taskTitle, String taskDesc, WorkStatus workStatus, Long workerId, String workerName, String workerThumbnailImg, Long sprintId) {
        this.taskTitle = taskTitle;
        this.taskDesc = taskDesc;
        this.workStatus = workStatus;
        this.workerId = workerId;
        this.workerName = workerName;
        this.workerThumbnailImg = workerThumbnailImg;
        this.sprintId = sprintId;
    }
}
