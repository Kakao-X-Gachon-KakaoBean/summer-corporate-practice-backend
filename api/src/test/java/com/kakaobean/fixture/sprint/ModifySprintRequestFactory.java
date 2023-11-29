package com.kakaobean.fixture.sprint;

import com.kakaobean.sprint.dto.request.ModifySprintRequest;

import java.time.LocalDate;

public class ModifySprintRequestFactory {

    private ModifySprintRequestFactory() {}

    public static ModifySprintRequest createRequest(){
        return new ModifySprintRequest(
                "수정된 스프린트 제목",
                "수정된 스프린트 내용",
                LocalDate.of(2023,7,25),
                LocalDate.of(2023,7,29)
        );
    }
}
