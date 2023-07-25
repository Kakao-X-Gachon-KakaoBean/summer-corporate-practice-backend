package com.kakaobean.unit.controller.factory.sprint;

import com.kakaobean.sprint.dto.request.RegisterSprintRequest;

import java.time.LocalDate;

public class RegisterSprintRequestFactory {

    private RegisterSprintRequestFactory() {}

    public static RegisterSprintRequest create(){
        return new RegisterSprintRequest(
                "스프린트 제목",
                "스프린트 설명",
                1L,
                LocalDate.of(2023,7,24),
                LocalDate.of(2023,7,30));
    }
}
