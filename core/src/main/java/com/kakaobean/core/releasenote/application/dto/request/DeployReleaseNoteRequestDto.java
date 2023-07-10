package com.kakaobean.core.releasenote.application.dto.request;

import com.kakaobean.core.releasenote.domain.ReleaseNote;
import lombok.Getter;

import static com.kakaobean.core.common.domain.BaseStatus.*;


@Getter
public class DeployReleaseNoteRequestDto {

    private final String title;
    private final String content;
    private final Double version;
    private final Long projectId;
    private final Long writerId;

    public DeployReleaseNoteRequestDto(String title,
                                       String content,
                                       Double version,
                                       Long projectId,
                                       Long writerId) {
        this.title = title;
        this.content = content;
        this.version = version;
        this.projectId = projectId;
        this.writerId = writerId;
    }

    public ReleaseNote toEntity() {
        return new ReleaseNote(ACTIVE, title, content, version, projectId, writerId);
    }
}
