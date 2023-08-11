package com.kakaobean.core.sprint.domain;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.sprint.exception.InvalidSprintDateException;
import com.kakaobean.core.sprint.exception.SprintAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;

@Component
@RequiredArgsConstructor
public class SprintValidator {

    private final ProjectMemberRepository projectMemberRepository;

    public void validate(Sprint sprint, Long memberId) {
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, sprint.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(projectMember.getProjectRole() != ADMIN){
            throw new SprintAccessException();
        }

        checkDateValidation(sprint.getStartDate(), sprint.getEndDate());
    }

    private void checkDateValidation(LocalDate startDate, LocalDate endDate){
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidSprintDateException();
        }
    }

    public void validateRemoveAccess(Sprint sprint, Long memberId) {
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, sprint.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(projectMember.getProjectRole() != ADMIN){
            throw new SprintAccessException();
        }
    }
}
