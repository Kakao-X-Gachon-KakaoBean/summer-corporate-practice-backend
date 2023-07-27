package com.kakaobean.core.factory.sprint;

import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.WorkStatus;

import java.time.LocalDate;

public class TaskFactory {

    private TaskFactory() {}

    public static Task createWithId(Long sprintId, Long workerId){
        return Task.builder()
                .title("테스크 제목")
                .content("테스크 내용")
                .sprintId(sprintId)
                .workerId(workerId)
                .workStatus(WorkStatus.NOT_ASSIGNED)
                .build();
    }
}
