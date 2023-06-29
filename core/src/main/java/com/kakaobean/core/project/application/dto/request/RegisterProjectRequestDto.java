package com.kakaobean.core.project.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.project.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterProjectRequestDto {

    private String title;

    private String content;

    @Builder
    public RegisterProjectRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Project toEntity(){
        return new
                Project(
                title,
                content,
                BaseStatus.ACTIVE
        );
    }
}
