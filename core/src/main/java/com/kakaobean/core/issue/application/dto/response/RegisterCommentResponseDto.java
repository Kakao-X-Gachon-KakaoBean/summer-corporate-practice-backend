package com.kakaobean.core.issue.application.dto.response;

public class RegisterCommentResponseDto {

    private final Long CommentId;

    private final String WrittenTime;

    public RegisterCommentResponseDto(Long commentId, String writtenTime){
        CommentId = commentId;
        WrittenTime = writtenTime;
    }
}

