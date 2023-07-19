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
    private Long projectId;

    @NotBlank
    private String title;

    private String content;

    @Builder
    public RegisterIssueRequest(Long projectId, String title, String content){
        this.projectId = projectId;
        this.title = title;
        this.content = content;
    }

    public RegisterIssueRequestDto toServiceDto(Long writerId){
        return new RegisterIssueRequestDto(
                projectId,
                title,
                content,
                writerId
        );
    }


}
