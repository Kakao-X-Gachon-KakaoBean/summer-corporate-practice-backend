package com.kakaobean.unit.controller.factory.sprint;

import com.kakaobean.core.sprint.domain.WorkStatus;
import com.kakaobean.core.sprint.domain.repository.query.FindTaskResponseDto;

public class FindTaskResponseDtoFactory {

    private FindTaskResponseDtoFactory() {
    }

    public static FindTaskResponseDto create() {
        return new FindTaskResponseDto(
                "로그인 기능 구현",
                "카카오, 구글 등 소셜 로그인을 포함한다.",
                WorkStatus.WORKING,
                1L,
                "임인범",
                "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png",
                1L
        );
    }
}
