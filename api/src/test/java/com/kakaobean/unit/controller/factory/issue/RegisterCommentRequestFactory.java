package com.kakaobean.unit.controller.factory.issue;

import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;

public class RegisterCommentRequestFactory {
    private RegisterCommentRequestFactory() {}

    public static RegisterCommentRequest create(){
        return new RegisterCommentRequest(
                "댓글 내용"
        );
    }
}
