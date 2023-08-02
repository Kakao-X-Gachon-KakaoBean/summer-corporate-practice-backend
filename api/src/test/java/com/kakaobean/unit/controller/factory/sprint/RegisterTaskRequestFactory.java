package com.kakaobean.unit.controller.factory.sprint;

import com.kakaobean.sprint.dto.request.RegisterTaskRequest;

public class RegisterTaskRequestFactory {

    private RegisterTaskRequestFactory() {}

    public static RegisterTaskRequest create(){
        return new RegisterTaskRequest(
                "테스크 제목",
                "테스크 내용",
                1L
        );
    }
    public static RegisterTaskRequest createWithId(Long sprintId){
        return new RegisterTaskRequest(
                "테스크 제목",
                "테스크 내용",
                sprintId
        );
    }
}
