package com.kakaobean.unit.controller.factory.project;

import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;

import java.util.List;


public class FindProjectResponseDtoFactory {

    private FindProjectResponseDtoFactory() {}

    public static List<FindProjectResponseDto> createList() {
        return List.of(
                new FindProjectResponseDto(5L, "Awesome project", "으썸한 프로젝트 설명"),
                new FindProjectResponseDto(6L, "kakaoBean Project", "카카오빈 프로젝트에 대한 설명")
        );
    }
}
