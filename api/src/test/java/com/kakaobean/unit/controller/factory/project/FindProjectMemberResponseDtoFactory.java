package com.kakaobean.unit.controller.factory.project;

import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;

import java.util.List;

import static com.kakaobean.core.project.domain.ProjectRole.*;

public class FindProjectMemberResponseDtoFactory {

    private FindProjectMemberResponseDtoFactory() {}

    public static List<FindProjectMemberResponseDto> createList() {
        return List.of(
                new FindProjectMemberResponseDto(5L, "exAdmin", "example1@gmail.com", ADMIN),
                new FindProjectMemberResponseDto(6L, "exMem1", "example2@gmail.com", MEMBER),
                new FindProjectMemberResponseDto(7L, "exMem2", "example3@gmail.com", VIEWER),
                new FindProjectMemberResponseDto(8L, "exViewer", "example4@gmail.com", INVITED_PERSON)
        );
    }
}
