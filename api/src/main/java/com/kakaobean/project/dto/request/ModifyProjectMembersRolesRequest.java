package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.ModifyProjectMemberRoleRequestDto;
import com.kakaobean.core.project.application.dto.request.ModifyProjectMembersRolesRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Getter
@NoArgsConstructor
public class ModifyProjectMembersRolesRequest {

    private List<ModifyProjectMemberRoleRequest> modifyProjectMemberRole;

    public ModifyProjectMembersRolesRequestDto toServiceDto(Long projectId, Long adminId) {
        return new ModifyProjectMembersRolesRequestDto(
                adminId,
                projectId,
                modifyProjectMemberRole
                        .stream()
                        .map(dto -> dto.toServiceDto())
                        .collect(toList())
        );
    }

    /**
     * 테스트 코드에서 사용
     */
    public ModifyProjectMembersRolesRequest(List<ModifyProjectMemberRoleRequest> modifyProjectMemberRole) {
        this.modifyProjectMemberRole = modifyProjectMemberRole;
    }
}
