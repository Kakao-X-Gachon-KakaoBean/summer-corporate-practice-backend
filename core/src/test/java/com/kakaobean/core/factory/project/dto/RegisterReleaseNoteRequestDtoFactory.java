package com.kakaobean.core.factory.project.dto;

import com.kakaobean.core.releasenote.application.dto.request.RegisterReleaseNoteRequestDto;

public class RegisterReleaseNoteRequestDtoFactory {

    private RegisterReleaseNoteRequestDtoFactory() {}

    public static RegisterReleaseNoteRequestDto create(){
        return new RegisterReleaseNoteRequestDto("release note title", "contents", 3.1, 1L, 2L);
    }

    public static RegisterReleaseNoteRequestDto createWithProjectIdAndWriterId(Long projectId, Long writerId){
        return new RegisterReleaseNoteRequestDto("release note title", "contents", 3.1, projectId, writerId);
    }
}
