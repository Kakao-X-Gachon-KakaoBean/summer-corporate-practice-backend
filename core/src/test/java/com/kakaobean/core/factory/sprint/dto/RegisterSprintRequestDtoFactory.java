package com.kakaobean.core.factory.sprint.dto;

import com.kakaobean.core.sprint.application.dto.RegisterSprintRequestDto;

import java.time.LocalDate;

public class RegisterSprintRequestDtoFactory {

    private RegisterSprintRequestDtoFactory() {}

    public static RegisterSprintRequestDto createDto(Long memberId, Long projectId){
        return new RegisterSprintRequestDto(
                "스프린트 제목",
                "스프린트 내용",
                projectId,
                LocalDate.of(2023,7,23),
                LocalDate.of(2023,7,30),
                memberId);
    }

    public static RegisterSprintRequestDto createFailDto(Long memberId, Long projectId){
        return new RegisterSprintRequestDto(
                "스프린트 제목",
                "스프린트 내용",
                projectId,
                LocalDate.of(2023,7,23),
                LocalDate.of(2023,7,03),
                memberId);
    }
}
