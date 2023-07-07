package com.kakaobean.unit.controller.factory.releasenote;


import com.kakaobean.releasenote.dto.request.RegisterReleaseNoteRequest;

public class RegisterReleaseNoteRequestFactory {

    private RegisterReleaseNoteRequestFactory() {}

    public static RegisterReleaseNoteRequest create(){
        return new RegisterReleaseNoteRequest("3.1V Release Note", "Contents..", 1.0, 10L);
    }
}
