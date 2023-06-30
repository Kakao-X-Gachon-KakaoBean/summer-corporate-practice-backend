package com.kakaobean.core.project.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.project.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterProjectRequestDto {

    private String title;
    private String content;
    private String secretKey;

    @Builder
    public RegisterProjectRequestDto(String title, String content, String secretKey) {
        this.title = title;
        this.content = content;
        this.secretKey = secretKey;
    }

    public Project toEntity(){
        return new
                Project(
                title,
                content,
                BaseStatus.ACTIVE,
                secretKey
        );
    }
}
