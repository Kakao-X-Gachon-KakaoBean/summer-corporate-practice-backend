package com.kakaobean.core.issue.application.dto.response;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import lombok.Getter;

@Getter
public class RegisterIssueResponseDto {

    private final Long IssueId;

    public RegisterIssueResponseDto(Long issueId){
        IssueId = issueId;
    }
}
