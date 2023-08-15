package com.kakaobean.issue.dto;

import com.kakaobean.core.issue.application.dto.request.ModifyCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyCommentRequest {

    private String content;

    public ModifyCommentRequest(String content) {
        this.content = content;
    }

    public ModifyCommentRequestDto toServiceDto(Long writerId, Long commentId){
        return new ModifyCommentRequestDto(
                commentId,
                writerId,
                content
        );
    }
}
