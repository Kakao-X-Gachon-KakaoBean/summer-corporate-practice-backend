package com.kakaobean.core.project.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyProjectInfoReqeustDto {

    private Long adminId;
    private Long projectId;
    private String newTitle;
    private String newContent;

    public ModifyProjectInfoReqeustDto(Long adminId,
                                       Long projectId,
                                       String newTitle,
                                       String newContent) {
        this.adminId = adminId;
        this.projectId = projectId;
        this.newTitle = newTitle;
        this.newContent = newContent;
    }
}
