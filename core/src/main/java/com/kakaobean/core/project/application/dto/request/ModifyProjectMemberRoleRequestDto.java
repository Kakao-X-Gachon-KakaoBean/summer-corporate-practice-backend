package com.kakaobean.core.project.application.dto.request;

import com.kakaobean.core.project.domain.ProjectRole;
import lombok.Getter;

import java.util.List;

@Getter
public class ModifyProjectMemberRoleRequestDto {

    private final Long modifyProjectMemberId;
    private final ProjectRole projectRole;

    public ModifyProjectMemberRoleRequestDto(Long modifyProjectMemberId, ProjectRole projectRole) {
        this.modifyProjectMemberId = modifyProjectMemberId;
        this.projectRole = projectRole;
    }
}
