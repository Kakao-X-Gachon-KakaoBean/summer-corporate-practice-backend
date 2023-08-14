package com.kakaobean.core.factory.issue.dto;

import com.kakaobean.core.issue.application.dto.request.ModifyIssueRequestDto;


public class ModifyIssueRequestDtoFactory {
    private  ModifyIssueRequestDtoFactory() {}

    public static ModifyIssueRequestDto createWithId(Long writerId, Long issueId){
        return new ModifyIssueRequestDto(
                "수정된 이슈 제목",
                "수정된 이슈 내용",
                writerId,
                issueId
        );
    }
}
