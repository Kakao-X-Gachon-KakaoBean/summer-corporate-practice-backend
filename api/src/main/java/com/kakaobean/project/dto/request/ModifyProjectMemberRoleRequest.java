package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.ModifyProjectMemberRoleRequestDto;
import com.kakaobean.core.project.domain.ProjectRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyProjectMemberRoleRequest {

    private Long modifyProjectMemberId;
    private ProjectRole projectRole;

    public ModifyProjectMemberRoleRequest(Long modifyProjectMemberId, ProjectRole projectRole) {
        this.modifyProjectMemberId = modifyProjectMemberId;
        this.projectRole = projectRole;
    }

    public ModifyProjectMemberRoleRequestDto toServiceDto(){
        return new ModifyProjectMemberRoleRequestDto(modifyProjectMemberId, projectRole);
    }
}
