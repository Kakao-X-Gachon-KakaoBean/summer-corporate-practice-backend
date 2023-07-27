package com.kakaobean.core.project.domain;

import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.project.exception.NotProjectInvitedPersonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectValidator {

    private final ProjectMemberRepository projectMemberRepository;

    public void validAdmin(Long projectMemberId, Long projectId) {
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(projectMemberId, projectId)
                .orElseThrow(NotExistsProjectMemberException::new);
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
