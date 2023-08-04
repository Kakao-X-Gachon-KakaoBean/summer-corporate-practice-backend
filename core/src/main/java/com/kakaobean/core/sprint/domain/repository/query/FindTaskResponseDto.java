package com.kakaobean.core.sprint.domain.repository.query;

import com.kakaobean.core.sprint.domain.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindTaskResponseDto {

    private String taskTitle;
    private String taskContent;
    private WorkStatus taskWorkStatus;
    private Long workerId;
    private String workerName;
    private String workerThumbnailImg;

    public FindTaskResponseDto(String taskTitle, String taskContent, WorkStatus taskWorkStatus, Long workerId, String workerName, String workerThumbnailImg) {
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskWorkStatus = taskWorkStatus;
        this.workerId = workerId;
        this.workerName = workerName;
        this.workerThumbnailImg = workerThumbnailImg;
    }
}
