package com.kakaobean.core.factory.sprint.dto;

import com.kakaobean.core.sprint.application.dto.ChangeWorkStatusRequestDto;

public class ChangeWorkStatusRequestDtoFactory {

    public static ChangeWorkStatusRequestDto createWithId(Long workerId, Long taskId){
        return new ChangeWorkStatusRequestDto(
                workerId,
                taskId,
                "complete"
        );
    }
}
