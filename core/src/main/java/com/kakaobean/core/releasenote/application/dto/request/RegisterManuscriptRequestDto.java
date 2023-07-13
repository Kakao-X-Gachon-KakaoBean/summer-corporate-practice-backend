package com.kakaobean.core.releasenote.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.releasenote.domain.Manuscript;

import static com.kakaobean.core.common.domain.BaseStatus.*;

public class RegisterManuscriptRequestDto {

    private final String title;
    private final String content;
    private final Double version;
    private final Long memberId;
    private final Long projectId;

    public RegisterManuscriptRequestDto(String title, String content, Double version, Long memberId, Long projectId) {
        this.title = title;
        this.content = content;
        this.version = version;
        this.memberId = memberId;
        this.projectId = projectId;
    }

    public Manuscript toEntity() {
        return new Manuscript(ACTIVE, title, content, version, memberId, projectId);
    }
}
