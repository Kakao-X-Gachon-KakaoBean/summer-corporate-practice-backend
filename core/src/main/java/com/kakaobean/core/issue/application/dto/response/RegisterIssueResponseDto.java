package com.kakaobean.core.issue.application.dto.response;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import lombok.Getter;

@Getter
public class RegisterIssueResponseDto {

    private final Long IssueId;

    private final String WrittenTime;


    public RegisterIssueResponseDto(Long issueId, String writtenTime){
        IssueId = issueId;
        WrittenTime = writtenTime;
    }
}
