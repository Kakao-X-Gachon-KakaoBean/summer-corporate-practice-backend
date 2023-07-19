package com.kakaobean.core.issue.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.issue.domain.Issue;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Getter
public class RegisterIssueRequestDto {

    private Long projectId;

    private String title;

    private String content;

    private Long writerId;

    @Builder
    public RegisterIssueRequestDto(Long projectId, String title, String content, Long writerId) {
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.writerId = writerId;
    }

    public Issue toEntity(){

        return new Issue(
                BaseStatus.ACTIVE,
                projectId,
                title,
                content,
                writerId
        );
    }

}
