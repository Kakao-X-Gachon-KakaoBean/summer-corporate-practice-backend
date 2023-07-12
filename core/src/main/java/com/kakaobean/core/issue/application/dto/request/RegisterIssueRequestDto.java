package com.kakaobean.core.issue.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.issue.domain.Issue;
import lombok.Builder;
import lombok.Getter;


@Getter
public class RegisterIssueRequestDto {

    private String title;

    private String content;

    private Long writerId;

    public Long projectId;

    @Builder
    public RegisterIssueRequestDto(String title, String content, Long writerId) {
        this.title = title;
        this.content = content;
        this.writerId = writerId;
    }

    public Issue toEntity(){

        return new Issue(
                BaseStatus.ACTIVE,
                //TODO: project ID is needed.
                projectId,
                title,
                content
        );
    }

}
