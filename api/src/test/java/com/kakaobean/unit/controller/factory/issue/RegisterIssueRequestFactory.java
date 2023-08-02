package com.kakaobean.unit.controller.factory.issue;

import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;

public class RegisterIssueRequestFactory {
    private RegisterIssueRequestFactory() {}

    public static RegisterIssueRequest create(){
        return new RegisterIssueRequest(
                "이슈 제목",
                "이슈 내용"
        );
    }
}
