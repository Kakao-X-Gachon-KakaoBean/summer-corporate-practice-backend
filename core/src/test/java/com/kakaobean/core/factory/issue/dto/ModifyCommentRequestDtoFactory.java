package com.kakaobean.core.factory.issue.dto;

import com.kakaobean.core.issue.application.dto.request.ModifyCommentRequestDto;

public class ModifyCommentRequestDtoFactory {
    private  ModifyCommentRequestDtoFactory() {}

    public static ModifyCommentRequestDto createWithIdAndMemberId(Long commentId, Long memberId) {
        return new ModifyCommentRequestDto(
                commentId,
                memberId,
                "수정된 댓글 내용"
        );
    }
}
