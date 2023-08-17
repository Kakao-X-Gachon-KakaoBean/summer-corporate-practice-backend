package com.kakaobean.core.factory.project.dto;

import com.kakaobean.core.project.domain.repository.query.FindProjectMemberResponseDto;

import java.util.List;

import static com.kakaobean.core.project.domain.ProjectRole.*;

public class FindProjectMemberResponseDtoFactory {

    private FindProjectMemberResponseDtoFactory() {}

    public static List<FindProjectMemberResponseDto> createList(){
        return List.of(
                new FindProjectMemberResponseDto(4L, "testMember1", "test1@gmail.com", MEMBER, "memberThumbnailImg"),
                new FindProjectMemberResponseDto(5L, "testMember2", "test2@gmail.com", ADMIN, "memberThumbnailImg"),
                new FindProjectMemberResponseDto(6L, "testMember3", "test3@gmail.com", VIEWER, "memberThumbnailImg"),
                new FindProjectMemberResponseDto(7L, "testMember4", "test4@gmail.com", MEMBER, "memberThumbnailImg")
        );
    }
}
