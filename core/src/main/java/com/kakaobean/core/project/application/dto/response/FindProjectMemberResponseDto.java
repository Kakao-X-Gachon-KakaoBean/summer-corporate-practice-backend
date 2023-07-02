package com.kakaobean.core.project.application.dto.response;

import com.kakaobean.core.project.domain.ProjectRole;
import lombok.Getter;

@Getter
public class FindProjectMemberResponseDto {

    private Long projectMemberId;
    private String projectMemberName;
    private String projectMemberEmail;
    private ProjectRole projectMemberRole;

    protected FindProjectMemberResponseDto() {}

    public FindProjectMemberResponseDto(Long projectMemberId,
                                        String projectMemberName,
                                        String projectMemberEmail,
                                        ProjectRole projectMemberRole) {
        this.projectMemberId = projectMemberId;
        this.projectMemberName = projectMemberName;
        this.projectMemberEmail = projectMemberEmail;
        this.projectMemberRole = projectMemberRole;
    }
}
