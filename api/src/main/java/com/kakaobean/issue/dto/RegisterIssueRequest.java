package com.kakaobean.issue.dto;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RegisterIssueRequest {

    @NotBlank
    private String title;

    private String content;

    @NotNull
    private Long projectId;

    @Builder
    public RegisterIssueRequest(String title, String content, Long projectId){
        this.title = title;
        this.content = content;
        this.projectId = projectId;
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
