package com.kakaobean.core.project.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ModifyProjectMembersRolesRequestDto {

    private final Long adminId;
    private final Long projectId;
    private List<ModifyProjectMemberRoleRequestDto> modifyProjectMemberRoles;

    public ModifyProjectMembersRolesRequestDto(Long adminId,
                                               Long projectId,
                                               List<ModifyProjectMemberRoleRequestDto> modifyProjectMemberRoles) {
        this.adminId = adminId;
        this.projectId = projectId;
        this.modifyProjectMemberRoles = modifyProjectMemberRoles;
    }
}
