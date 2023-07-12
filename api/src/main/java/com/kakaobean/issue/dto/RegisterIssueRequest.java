package com.kakaobean.issue.dto;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RegisterIssueRequest {

    @NotBlank
    private String title;

    private String content;

    @Builder
    public RegisterIssueRequest(String title, String content){
        this.title = title;
        this.content = content;
    }

    public RegisterIssueRequestDto toServiceDto(Long writerId){
        return new RegisterIssueRequestDto(
                title,
                content,
                writerId
        );
    }


}
