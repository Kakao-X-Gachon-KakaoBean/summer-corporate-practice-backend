package com.kakaobean.core.factory.project.dto;

import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.project.domain.ProjectRole;

import java.util.List;

import static com.kakaobean.core.project.domain.ProjectRole.*;

public class FindProjectMemberResponseDtoFactory {

    private FindProjectMemberResponseDtoFactory() {}

    public static List<FindProjectMemberResponseDto> createList(){
        return List.of(
                new FindProjectMemberResponseDto(4L, "testMember1", "test1@gmail.com", MEMBER),
                new FindProjectMemberResponseDto(5L, "testMember2", "test2@gmail.com", ADMIN),
                new FindProjectMemberResponseDto(6L, "testMember3", "test3@gmail.com", VIEWER),
                new FindProjectMemberResponseDto(7L, "testMember4", "test4@gmail.com", MEMBER)
        );
    }
}
