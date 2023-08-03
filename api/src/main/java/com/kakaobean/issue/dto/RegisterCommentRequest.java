package com.kakaobean.issue.dto;

import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RegisterCommentRequest {

    private String content;

    private Long issueId;

    @Builder
    public RegisterCommentRequest(String content, Long issueId){
        this.content = content;
        this.issueId = issueId;
    }

    public RegisterCommentRequestDto toServiceDto(Long writerId){
        return new RegisterCommentRequestDto(
                issueId,
                writerId,
                content
        );
    }

}
