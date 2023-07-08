package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoReqeustDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ModifyProjectInfoRequest {

    @NotBlank
    private String newTitle;

    @NotBlank
    private String newContent;

    public ModifyProjectInfoRequest(String newTitle, String newContent) {
        this.newTitle = newTitle;
        this.newContent = newContent;
    }

    public ModifyProjectInfoReqeustDto toServiceDto(Long adminId, Long projectId){
        return new ModifyProjectInfoReqeustDto(
                adminId,
                projectId,
                newTitle,
                newContent
        );
    }
}
