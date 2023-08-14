package com.kakaobean.core.issue.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyCommentRequestDto {

    private Long commentId;

    private Long writerId;

    private String content;

    public ModifyCommentRequestDto(Long commentId, Long writerId, String content) {
        this.writerId = writerId;
        this.commentId = commentId;
        this.content = content;
    }
}
