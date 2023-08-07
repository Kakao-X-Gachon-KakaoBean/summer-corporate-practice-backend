package com.kakaobean.core.factory.issue.dto;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;

public class RegisterIssueRequestDtoFactory {
    public RegisterIssueRequestDtoFactory() {}

    public static RegisterIssueRequestDto createWithId(Long projectId, Long writerId){
        return new RegisterIssueRequestDto(
                projectId,
                "이슈 제목",
                "이슈 내용",
                writerId
        );
    }
}
