package com.kakaobean.core.factory.issue;

import com.kakaobean.core.issue.domain.Comment;

public class CommentFactory {
    private CommentFactory() {}

    public static Comment create(){
        return Comment.builder()
                .issueId(2L)
                .content("이슈 내용")
                .writerId(3L)
                .build();
    }


    public static Comment createComment(Long issueId){
        return Comment.builder()
                .issueId(issueId)
                .content("이슈 내용")
                .writerId(2L)
                .build();
    }

    public static Comment createCommentWithMemberIdAndIssueId(Long memberId, Long issueId){
        return Comment.builder()
                .issueId(issueId)
                .content("이슈 내용")
                .writerId(memberId)
                .build();
    }
}