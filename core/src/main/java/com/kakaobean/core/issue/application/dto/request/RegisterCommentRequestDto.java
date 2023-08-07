package com.kakaobean.core.issue.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.Issue;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterCommentRequestDto {

    private Long writerId;

    private Long issueId;

    private String content;

    @Builder
    public RegisterCommentRequestDto(Long issueId, Long writerId, String content) {
        this.issueId = issueId;
        this.writerId = writerId;
        this.content = content;
    }

    public Comment toEntity(){

        return new Comment(
                BaseStatus.ACTIVE,
                issueId,
                content,
                writerId
        );
    }

}
