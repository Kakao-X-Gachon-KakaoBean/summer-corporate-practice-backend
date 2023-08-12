package com.kakaobean.core.factory.issue;

import com.kakaobean.core.issue.domain.Issue;

public class IssueFactory {
    private IssueFactory() {}

    public static Issue createIssue(Long projectId){
        return Issue.builder()
                .title("이슈 제목")
                .content("이슈 내용")
                .projectId(projectId)
                .writerId(2L)
                .build();
    }

    public static Issue createIssueWithMemberIdAndProjectId(Long memberId, Long projectId){
        return Issue.builder()
                .title("이슈 제목")
                .content("이슈 내용")
                .projectId(projectId)
                .writerId(memberId)
                .build();
    }
}
