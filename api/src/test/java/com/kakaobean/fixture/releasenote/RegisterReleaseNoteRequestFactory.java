package com.kakaobean.fixture.releasenote;


import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;

public class RegisterReleaseNoteRequestFactory {

    private RegisterReleaseNoteRequestFactory() {}

    public static DeployReleaseNoteRequest create(){
        return new DeployReleaseNoteRequest("3.1V Release Note", "Contents..", "1.0", 10L);
    }
}
