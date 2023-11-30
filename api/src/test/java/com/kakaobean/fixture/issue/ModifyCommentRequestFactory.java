package com.kakaobean.fixture.issue;

import com.kakaobean.issue.dto.ModifyCommentRequest;

public class ModifyCommentRequestFactory {

    private ModifyCommentRequestFactory() {}

    public static ModifyCommentRequest createRequest(){
        return new ModifyCommentRequest(
                "수정된 댓글 제목"
        );
    }
}
