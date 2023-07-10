package com.kakaobean.core.factory.project.dto;

import com.kakaobean.core.project.application.dto.request.ModifyProjectMemberRoleRequestDto;
import com.kakaobean.core.project.application.dto.request.ModifyProjectMembersRolesRequestDto;

import java.util.List;

import static com.kakaobean.core.project.domain.ProjectRole.*;

public class ModifyProjectMembersRolesRequestDtoFactory {

    private ModifyProjectMembersRolesRequestDtoFactory() {}

    public static ModifyProjectMembersRolesRequestDto create(){
        return new ModifyProjectMembersRolesRequestDto(
                2L,
                1L,
                List.of(
                        new ModifyProjectMemberRoleRequestDto(3L , ADMIN),
                        new ModifyProjectMemberRoleRequestDto(4L ,VIEWER)
                )
        );
    }

    public static ModifyProjectMembersRolesRequestDto create(Long adminId,
                                                             Long projectId,
                                                             Long memberId1,
                                                             Long memberId2){
        return new ModifyProjectMembersRolesRequestDto(
                adminId,
                projectId,
                List.of(
                        new ModifyProjectMemberRoleRequestDto(memberId1, ADMIN),
                        new ModifyProjectMemberRoleRequestDto(memberId2, VIEWER)
                )
        );
    }
}
