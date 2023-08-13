package com.kakaobean.issue.dto;

import com.kakaobean.core.issue.application.dto.request.ModifyIssueRequestDto;
import com.kakaobean.core.sprint.application.dto.ModifySprintRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ModifyIssueRequest {
    @NotBlank
    private String title;

    private String content;



    public ModifyIssueRequest(String title, String content) {
       this.title = title;
       this.content = content;
    }

    public ModifyIssueRequestDto toServiceDto(Long writerId, Long issueId){
        return new ModifyIssueRequestDto(
                title,
                content,
                writerId,
                issueId
        );
    }
}
