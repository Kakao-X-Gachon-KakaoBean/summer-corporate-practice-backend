package com.kakaobean.core.project.domain;

import com.kakaobean.core.project.exception.NotProjectAdminException;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

    public void validAdmin(ProjectMember projectMember){
        if(projectMember.getProjectRole() != ProjectRole.ADMIN){
            throw new NotProjectAdminException();
        }
    }
}
