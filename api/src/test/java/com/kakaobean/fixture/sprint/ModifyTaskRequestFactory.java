package com.kakaobean.fixture.sprint;

import com.kakaobean.sprint.dto.request.ModifyTaskRequest;

public class ModifyTaskRequestFactory {

    private ModifyTaskRequestFactory() {}

    public static ModifyTaskRequest createRequest(){
        return new ModifyTaskRequest(
                "수정된 테스크 제목",
                "수정된 테스크 내용",
                1L
        );
    }

    public static ModifyTaskRequest createRequestWithId(Long sprintId){
        return new ModifyTaskRequest(
                "수정된 테스크 제목",
                "수정된 테스크 내용",
                sprintId
        );
    }
}
