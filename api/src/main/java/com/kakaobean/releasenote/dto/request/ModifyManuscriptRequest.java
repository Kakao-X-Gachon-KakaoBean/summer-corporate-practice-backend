package com.kakaobean.releasenote.dto.request;

import com.kakaobean.core.releasenote.application.dto.request.ModifyManuscriptRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class ModifyManuscriptRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String version;

    public ModifyManuscriptRequest(String title, String content, String version) {
        this.title = title;
        this.content = content;
        this.version = version;
    }

    public ModifyManuscriptRequestDto toServiceDto(Long memberId, Long manuscriptId) {
        return new ModifyManuscriptRequestDto(title, content, version, memberId, manuscriptId);
    }
}
