package com.kakaobean.core.sprint.domain;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.sprint.Exception.TaskAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final ProjectMemberRepository projectMemberRepository;

    public void validate(Long memberId, Long projectId){
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, projectId)
                .orElseThrow(NotExistsProjectMemberException::new);

        if (projectMember.getProjectRole() != ADMIN){
            throw new TaskAccessException();
        }
    }

}
