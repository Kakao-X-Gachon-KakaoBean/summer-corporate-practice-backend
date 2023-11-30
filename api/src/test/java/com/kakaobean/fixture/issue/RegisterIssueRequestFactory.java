package com.kakaobean.fixture.issue;

import com.kakaobean.issue.dto.RegisterIssueRequest;

public class RegisterIssueRequestFactory {
    private RegisterIssueRequestFactory() {}

    public static RegisterIssueRequest create(){
        return new RegisterIssueRequest(
                "이슈 제목",
                "이슈 내용",
                1L
        );
    }

    public static RegisterIssueRequest createWithProjectId(Long projectId){
        return new RegisterIssueRequest(
                "이슈 제목",
                "이슈 내용",
                projectId
        );
    }
}
