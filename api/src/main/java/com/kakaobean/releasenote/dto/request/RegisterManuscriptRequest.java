package com.kakaobean.releasenote.dto.request;

import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RegisterManuscriptRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    private Double version;

    @NotNull
    private Long projectId;

    public RegisterManuscriptRequest(String title, String content, Double version, Long projectId) {
        this.title = title;
        this.content = content;
        this.version = version;
        this.projectId = projectId;
    }

    public RegisterManuscriptRequestDto toServiceDto(Long memberId) {
        return new RegisterManuscriptRequestDto(title, content, version, memberId, projectId);
    }
}
