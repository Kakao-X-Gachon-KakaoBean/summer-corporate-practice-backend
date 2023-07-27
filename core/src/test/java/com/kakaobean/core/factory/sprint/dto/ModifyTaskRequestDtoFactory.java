package com.kakaobean.core.factory.sprint.dto;

import com.kakaobean.core.sprint.application.dto.ModifyTaskRequestDto;

public class ModifyTaskRequestDtoFactory {

    private ModifyTaskRequestDtoFactory() {}

    public static ModifyTaskRequestDto createWithId(Long taskId, Long sprintId, Long adminId){
        return new ModifyTaskRequestDto(
                taskId,
                "새로운 테스크 제목",
                "새로운 테스크 내용",
                sprintId,
                adminId
        );
    }
}
