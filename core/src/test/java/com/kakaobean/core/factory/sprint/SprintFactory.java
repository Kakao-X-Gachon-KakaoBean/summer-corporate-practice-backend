package com.kakaobean.core.factory.sprint;

import com.kakaobean.core.sprint.domain.Sprint;

import java.time.LocalDate;

public class SprintFactory {
    private SprintFactory() {}

    public static Sprint createWithId(Long projectId){
        return Sprint.builder()
                .title("스프린트 제목")
                .description("스프린트 설명")
                .startDate(LocalDate.of(2023,8,1))
                .endDate(LocalDate.of(2023,8,31))
                .projectId(projectId)
                .build();
    }

}
