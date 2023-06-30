package com.kakaobean.core.project.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.project.domain.Project;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegisterProjectRequestDto {

    private String title;
    private String content;
    private Long adminId;

    @Builder
    public RegisterProjectRequestDto(String title, String content, Long adminId) {
        this.title = title;
        this.content = content;
        this.adminId = adminId;
    }

    public Project toEntity(){
        return new
                Project(
                title,
                content,
                BaseStatus.ACTIVE,
                UUID.randomUUID().toString()
        );
    }
}
