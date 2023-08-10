package com.kakaobean.unit.controller.factory.issue;

import com.kakaobean.core.issue.domain.repository.query.FindIndividualIssueResponseDto;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;

import java.util.List;

public class FindIndividualIssueResponseDtoFactory {
    private FindIndividualIssueResponseDtoFactory(){}

    public static FindIndividualIssueResponseDto create() {
        return new FindIndividualIssueResponseDto(
                11L,
                "이슈 제목",
                "이슈 내용",
                "23. 8. 7. ?? 9:49",
                "Hiki",
                "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png",
                List.of(
                        new FindIndividualIssueResponseDto.CommentDto(
                                33L,
                                "1번 댓글",
                                " 23. 8. 7. ?? 9:49",
                                "SubinGay",
                                "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png"
                        ),
                        new FindIndividualIssueResponseDto.CommentDto(
                                44L,
                                "2번 댓글",
                                " 23. 8. 7. ?? 9:49",
                                "InbumShiChi",
                                "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png"
                                )
                )
        );
    }
}
