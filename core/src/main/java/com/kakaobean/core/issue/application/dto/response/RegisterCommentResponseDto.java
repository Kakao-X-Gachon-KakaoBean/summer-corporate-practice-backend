package com.kakaobean.core.issue.application.dto.response;

import lombok.Getter;

@Getter
public class RegisterCommentResponseDto {

    private final Long CommentId;

    public RegisterCommentResponseDto(Long commentId){
        CommentId = commentId;
    }
}

