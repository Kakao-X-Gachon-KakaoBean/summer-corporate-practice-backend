package com.kakaobean.fixture.issue;

import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;

import java.util.List;

public class FindIssuesWithinPageResponseDtoFactory {

    private FindIssuesWithinPageResponseDtoFactory(){}

    public static FindIssuesWithinPageResponseDto create() {
        return new FindIssuesWithinPageResponseDto(
                true,
                List.of(
                        new FindIssuesWithinPageResponseDto.IssueDto(
                                1L,
                                "1번 이슈",
                                1L,
                                "Jason",
                                " 23. 8. 7. ?? 9:49"
                        ),
                        new FindIssuesWithinPageResponseDto.IssueDto(
                                2L,
                                "2번 이슈",
                                2L,
                                "Anne",
                                "23. 8. 7. ?? 10:00"                        )
                )
        );
    }
}
