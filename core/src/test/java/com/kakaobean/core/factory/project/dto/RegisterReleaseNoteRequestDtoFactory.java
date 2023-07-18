package com.kakaobean.core.factory.project.dto;

import com.kakaobean.core.releasenote.application.dto.request.DeployReleaseNoteRequestDto;

public class RegisterReleaseNoteRequestDtoFactory {

    private RegisterReleaseNoteRequestDtoFactory() {}

    public static DeployReleaseNoteRequestDto create(){
        return new DeployReleaseNoteRequestDto("release note title", "contents", "3.1", 1L, 2L);
    }

    public static DeployReleaseNoteRequestDto createWithProjectIdAndWriterId(Long projectId, Long writerId){
        return new DeployReleaseNoteRequestDto("release note title", "contents", "3.1", projectId, writerId);
    }
}
