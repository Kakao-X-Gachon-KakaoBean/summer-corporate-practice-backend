package com.kakaobean.core.sprint.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeWorkStatusRequestDto {

    private Long workerId;
    private Long taskId;
    private String workStatus;

    public ChangeWorkStatusRequestDto(Long workerId, Long taskId, String workStatus) {
        this.workerId = workerId;
        this.taskId = taskId;
        this.workStatus = workStatus;
    }
}
