package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RegisterProjectRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String secretKey;

    @Builder
    public RegisterProjectRequest(String title, String content, String secretKey) {
        this.title = title;
        this.content = content;
        this.secretKey = secretKey;
    }





    public RegisterProjectRequestDto toServiceDto(){
        return new RegisterProjectRequestDto(
                title,
                content,
                secretKey
        );
    }
}
