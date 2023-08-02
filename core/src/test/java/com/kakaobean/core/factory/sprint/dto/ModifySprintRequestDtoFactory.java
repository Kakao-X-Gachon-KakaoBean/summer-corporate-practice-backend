package com.kakaobean.core.factory.sprint.dto;

import com.kakaobean.core.sprint.application.dto.ModifySprintRequestDto;

import java.time.LocalDate;

public class ModifySprintRequestDtoFactory {

    private ModifySprintRequestDtoFactory() {}

    public static ModifySprintRequestDto createWithId(Long adminId, Long sprintId){
        return new ModifySprintRequestDto(
                "수정된 스프린트 제목",
                "수정된 스프린트 내용",
                LocalDate.of(2023,8,23),
                LocalDate.of(2023,8,30),
                adminId,
                sprintId
        );
    }

    public static ModifySprintRequestDto createFailWithId(Long adminId, Long sprintId){
        return new ModifySprintRequestDto(
                "수정된 스프린트 제목",
                "수정된 스프린트 내용",
                LocalDate.of(2023,8,23),
                LocalDate.of(2023,8,03),
                adminId,
                sprintId
        );
    }
}
