package com.kakaobean.core.factory.project;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;

public class ProjectMemberFactory {

    private ProjectMemberFactory() {}

    public static ProjectMember createAdmin(){
        return new ProjectMember(ACTIVE, 1L ,2L, ProjectRole.ADMIN);
    }

    public static ProjectMember createMember(){
        return new ProjectMember(ACTIVE, 1L ,3L, ProjectRole.MEMBER);
    }

    public static ProjectMember createInvitedPerson(){
        return new ProjectMember(ACTIVE, 1L ,4L, ProjectRole.INVITED_PERSON);
    }


    public static ProjectMember createWithMemberIdAndProjectId(Long memberId, Long projectId, ProjectRole role){
        return new ProjectMember(ACTIVE, projectId, memberId, role);
    }

}
