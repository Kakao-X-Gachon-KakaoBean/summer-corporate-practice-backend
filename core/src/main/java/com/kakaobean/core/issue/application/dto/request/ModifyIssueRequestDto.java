package com.kakaobean.core.issue.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyIssueRequestDto {

    private String title;

    private String content;

    private Long writerId;

    private Long issueId;

    public ModifyIssueRequestDto(String title,  String content, Long writerId, Long issueId) {
        this.title = title;
        this.content = content;
        this.writerId = writerId;
        this.issueId = issueId;
    }
}