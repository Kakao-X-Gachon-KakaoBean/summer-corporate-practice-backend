package com.kakaobean.fixture.issue;

import com.kakaobean.issue.dto.ModifyIssueRequest;

public class ModifyIssueRequestFactory {

    private ModifyIssueRequestFactory() {}

    public static ModifyIssueRequest createRequest(){
        return new ModifyIssueRequest(
                "수정된 이슈 제목",
                "수정된 이슈 내용"
        );
    }
}
