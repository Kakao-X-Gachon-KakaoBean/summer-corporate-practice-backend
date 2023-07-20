package com.kakaobean.issue.dto;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RegisterIssueRequest {

    @NotBlank
    private String title;

    private String content;

    @Builder
    public RegisterIssueRequest(String title, String content){
        this.title = title;
        this.content = content;
    }

    public RegisterIssueRequestDto toServiceDto(Long projectId, Long writerId){
        return new RegisterIssueRequestDto(
                projectId,
                title,
                content,
                writerId
        );
    }


}
