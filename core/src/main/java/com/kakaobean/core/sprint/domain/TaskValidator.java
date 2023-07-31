package com.kakaobean.core.sprint.domain;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.sprint.Exception.AssignmentNotAllowedException;
import com.kakaobean.core.sprint.Exception.NotExistsSprintException;
import com.kakaobean.core.sprint.Exception.TaskAccessException;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;
import static com.kakaobean.core.project.domain.ProjectRole.VIEWER;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final ProjectMemberRepository projectMemberRepository;
    private final SprintRepository sprintRepository;

    public void validate(Long adminId, Long sprintId){
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(NotExistsSprintException::new);
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(adminId, sprint.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);
        if (projectMember.getProjectRole() != ADMIN){
            throw new TaskAccessException();
        }
    }

    public void validAssignmentTask(Long adminId, Long sprintId, Long workerId){
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(NotExistsSprintException::new);

        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(adminId, sprint.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);
        if (projectAdmin.getProjectRole() != ADMIN){
            throw new TaskAccessException();
        }

        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(workerId, sprint.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);
        if (projectMember.getProjectRole() == VIEWER){
            throw new AssignmentNotAllowedException();
        }
    }

}
