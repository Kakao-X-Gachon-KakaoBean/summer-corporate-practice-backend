package com.kakaobean.core.factory.issue.dto;

import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;

public class RegisterCommentRequestDtoFactory {
    public RegisterCommentRequestDtoFactory() {}

    public static RegisterCommentRequestDto createWithId(Long issueId, Long writerId){
        return new RegisterCommentRequestDto(
                issueId,
                writerId,
                "댓글 내용"
        );
    }
}
