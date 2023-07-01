package com.kakaobean.core.project.domain;

import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.project.exception.NotProjectInvitedPersonException;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

    public void validAdmin(ProjectMember projectMember){
        if(projectMember.getProjectRole() != ProjectRole.ADMIN){
            throw new NotProjectAdminException();
        }
    }

    public void validInvitedPerson(ProjectMember projectMember) {
        if(projectMember.getProjectRole() != ProjectRole.INVITED_PERSON){
            throw new NotProjectInvitedPersonException();
        }
    }
}
