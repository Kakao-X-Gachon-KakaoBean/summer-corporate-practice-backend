package com.kakaobean.core.factory.project;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;

public class ProjectMemberFactory {

    private ProjectMemberFactory() {}

    private static AtomicLong memberId = new AtomicLong(1000);
    private static AtomicLong projectId = new AtomicLong(1000);

    public static ProjectMember createAdmin(){
        return new ProjectMember(ACTIVE, projectId.getAndIncrement(), memberId.getAndIncrement(), ProjectRole.ADMIN);
    }

    public static ProjectMember createMember(){
        return new ProjectMember(ACTIVE, projectId.getAndIncrement(), memberId.getAndIncrement(), ProjectRole.MEMBER);
    }

    public static ProjectMember createInvitedPerson(){
        return new ProjectMember(ACTIVE, projectId.getAndIncrement(), memberId.getAndIncrement(), ProjectRole.INVITED_PERSON);
    }

    public static ProjectMember createWithMemberIdAndProjectId(Long memberId, Long projectId, ProjectRole role){
        return new ProjectMember(ACTIVE, projectId, memberId, role);
    }

    public static ProjectMember createWithProjectId(Long projectId, ProjectRole role){
        return new ProjectMember(ACTIVE, projectId, memberId.getAndIncrement(), role);
    }

    public static ProjectMember createViewer(){
        return new ProjectMember(ACTIVE, projectId.getAndIncrement() ,memberId.getAndIncrement(), ProjectRole.VIEWER);
    }
}
