package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ModifyProjectRequest {

    @NotBlank
    private String newTitle;

    @NotBlank
    private String newContent;

    public ModifyProjectRequest(String newTitle, String newContent) {
        this.newTitle = newTitle;
        this.newContent = newContent;
    }

    public ModifyProjectInfoRequestDto toServiceDto(Long adminId, Long projectId){
        return new ModifyProjectInfoRequestDto(
                adminId,
                projectId,
                newTitle,
                newContent
        );
    }
}
