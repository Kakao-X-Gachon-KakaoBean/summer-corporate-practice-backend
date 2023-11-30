package com.kakaobean.fixture.sprint;

import com.kakaobean.core.sprint.domain.repository.query.FindAllSprintResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.SprintsDto;
import com.kakaobean.core.sprint.domain.repository.query.TasksDto;

import java.time.LocalDate;
import java.util.List;

public class FindAllSprintResponseDtoFactory {

    private FindAllSprintResponseDtoFactory() {}

    public static FindAllSprintResponseDto create() {
        return new FindAllSprintResponseDto(
                List.of(
                        new SprintsDto(1L,
                                "멤버",
                                LocalDate.of(2023, 8, 1),
                                LocalDate.of(2023, 8, 10),
                                List.of(
                                        new TasksDto(
                                                1L,
                                                "회원가입"
                                        ),
                                        new TasksDto(
                                                2L,
                                                "로그인"
                                        )
                                )
                        ),
                        new SprintsDto(2L,
                                "상품",
                                LocalDate.of(2023, 8, 3),
                                LocalDate.of(2023, 8, 8),
                                List.of(
                                        new TasksDto(
                                                3L,
                                                "상품 조회"
                                        )
                                )
                        )
                )
        );
    }
}
