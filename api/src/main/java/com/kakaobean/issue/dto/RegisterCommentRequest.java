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

    @Builder
    public RegisterCommentRequest(String content){
        this.content = content;
    }

    public RegisterCommentRequestDto toServiceDto(Long issueId, Long writerId){
        return new RegisterCommentRequestDto(
                issueId,
                writerId,
                content
        );
    }

}
