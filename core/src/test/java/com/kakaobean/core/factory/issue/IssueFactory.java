package com.kakaobean.core.factory.issue;

import com.kakaobean.core.issue.domain.Issue;

public class IssueFactory {
    public IssueFactory() {}

    public static Issue create(Long projectId){
        return Issue.builder()
                .id(1L)
                .title("이슈 제목")
                .content("이슈 내용")
                .projectId(projectId)
                .build();
    }
}
