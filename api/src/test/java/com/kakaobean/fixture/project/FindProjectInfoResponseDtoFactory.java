package com.kakaobean.fixture.project;

import com.kakaobean.core.project.domain.repository.query.FindProjectInfoResponseDto;
import com.kakaobean.core.project.domain.repository.query.FindProjectMemberResponseDto;
import lombok.Getter;

import java.util.List;

import static com.kakaobean.core.project.domain.ProjectRole.*;

@Getter
public class FindProjectInfoResponseDtoFactory {

    private FindProjectInfoResponseDtoFactory() {}

    public static FindProjectInfoResponseDto create(){
        return new FindProjectInfoResponseDto(
                "프로젝트 이름",
                "프로젝트 설명",
                List.of(
                        new FindProjectMemberResponseDto(5L, "exAdmin", "example1@gmail.com", ADMIN, "memberThumbnailImg"),
                        new FindProjectMemberResponseDto(6L, "exMem1", "example2@gmail.com", MEMBER, "memberThumbnailImg"),
                        new FindProjectMemberResponseDto(7L, "exMem2", "example3@gmail.com", VIEWER, "memberThumbnailImg"),
                        new FindProjectMemberResponseDto(8L, "exViewer", "example4@gmail.com", INVITED_PERSON, "memberThumbnailImg")
                )
        );
    }
}
