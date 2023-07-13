package com.kakaobean.releasenote.dto.request;

import com.kakaobean.core.releasenote.application.dto.request.DeployReleaseNoteRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class DeployReleaseNoteRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String version;

    @NotNull
    private Long projectId;

    public DeployReleaseNoteRequest(String title, String content, String version, Long projectId) {
        this.title = title;
        this.content = content;
        this.version = version;
        this.projectId = projectId;
    }

    public DeployReleaseNoteRequestDto toServiceDto(Long memberId) {
        return new DeployReleaseNoteRequestDto(title, content, version, projectId, memberId);
    }
}
