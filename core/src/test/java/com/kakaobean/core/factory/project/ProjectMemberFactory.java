package com.kakaobean.core.factory.project;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.project.domain.Project;
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
}
