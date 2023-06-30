package com.kakaobean.core.project.domain;

import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

    public void validAdmin(ProjectMember projectMember){
        if(projectMember.getProjectRole() != ProjectRole.ADMIN){
            throw new RuntimeException();
        }
    }
}
