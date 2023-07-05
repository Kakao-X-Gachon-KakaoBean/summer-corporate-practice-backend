package com.kakaobean.releasenote.dto.request;

import com.kakaobean.core.releasenote.application.dto.request.RegisterReleaseNoteRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class RegisterReleaseNoteRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private Double version;

    @NotNull
    private Long projectId;

    public RegisterReleaseNoteRequest(String title, String content, Double version) {
        this.title = title;
        this.content = content;
        this.version = version;
    }

    public RegisterReleaseNoteRequestDto toServiceDto(Long memberId) {
        return new RegisterReleaseNoteRequestDto(title, content, version, projectId, memberId);
    }
}
