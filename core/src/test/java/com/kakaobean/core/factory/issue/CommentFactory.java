package com.kakaobean.core.factory.issue;

import com.kakaobean.core.issue.domain.Comment;

public class CommentFactory {
    private CommentFactory() {}

    public static Comment createComment(Long issueId){
        return Comment.builder()
                .commentId(1L)
                .issueId(issueId)
                .content("이슈 내용")
                .writerId(2L)
                .build();
    }
}