package com.kakaobean.unit.controller.factory.sprint;

import com.kakaobean.core.sprint.domain.WorkStatus;
import com.kakaobean.core.sprint.domain.repository.query.FindSprintResponseDto;

import java.time.LocalDate;
import java.util.List;

public class FindSprintResponseDtoFactory {

    private FindSprintResponseDtoFactory() {
    }

    public static FindSprintResponseDto create() {
        return new FindSprintResponseDto(
                "멤버 도메인",
                "설명 블라블라",
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2023, 8, 10),
                List.of(
                        new FindSprintResponseDto.TaskDto(
                                1L,
                                "회원가입",
                                WorkStatus.WORKING,
                                1L,
                                "조연겸",
                                "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png"
                        ),
                        new FindSprintResponseDto.TaskDto(
                                2L,
                                "로그인",
                                WorkStatus.COMPLETE,
                                2L,
                                "추성준",
                                "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png"
                        )
                )
        );
    }
}
