package com.kakaobean.core.factory.task;

import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.WorkStatus;

import java.time.LocalDate;

public class TaskFactory {

    private TaskFactory() {}

    public static Task createTask(Long sprintId, Long workerId){
        return Task.builder()
                .id(1L)
                .title("테스크 제목")
                .startDate(LocalDate.of(2023, 8, 10))
                .endDate(LocalDate.of(2023, 8, 20))
                .sprintId(sprintId)
                .workerId(workerId)
                .workStatus(WorkStatus.NOT_ASSIGNED)
                .build();
    }
}
