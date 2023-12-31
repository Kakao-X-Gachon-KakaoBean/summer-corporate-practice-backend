package com.kakaobean.core.issue.domain;

import com.kakaobean.core.issue.exception.IssueAccessException;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IssueValidator {

    private final ProjectMemberRepository projectMemberRepository;

    public void validate(Issue issue, Long memberId) {
        projectMemberRepository.findByMemberIdAndProjectId(memberId, issue.getProjectId()).
                orElseThrow(NotExistsProjectMemberException::new);
    }

    public void validateAccess(Issue issue, Long memberId) {
        projectMemberRepository.findByMemberIdAndProjectId(memberId, issue.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(!Objects.equals(memberId, issue.getWriterId())){
            throw new IssueAccessException();
        }
    }
}