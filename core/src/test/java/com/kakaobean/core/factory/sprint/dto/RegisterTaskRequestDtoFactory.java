package com.kakaobean.core.factory.sprint.dto;

import com.kakaobean.core.sprint.application.dto.RegisterTaskRequestDto;

public class RegisterTaskRequestDtoFactory {

    private RegisterTaskRequestDtoFactory() {}

    public static RegisterTaskRequestDto createWithId(Long sprintId, Long memberId){
        return new RegisterTaskRequestDto(
                "테스크 제목",
                "테스크 내용",
                sprintId,
                memberId
        );
    }
}
