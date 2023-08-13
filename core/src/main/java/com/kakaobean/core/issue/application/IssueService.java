package com.kakaobean.core.issue.application;

import com.kakaobean.core.issue.application.dto.request.ModifyIssueRequestDto;
import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.IssueValidator;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.NotExistsIssueException;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.sprint.application.dto.ModifySprintRequestDto;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.exception.NotExistsSprintException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private final CommentRepository commentRepository;

    private final IssueValidator issueValidator;


    @Transactional(readOnly = false)
    public void registerIssue(RegisterIssueRequestDto dto) {
        Issue issue = dto.toEntity();
        issueValidator.validate(issue, dto.getWriterId());
        issueRepository.save(issue);
    }

    @Transactional
    public void removeIssue(Long memberId, Long issueId){
        Issue issue = issueRepository.findById(issueId).orElseThrow(NotExistsIssueException::new);

        issueValidator.validateRemoveAccess(issue, memberId);

        commentRepository.deleteByIssueId(issueId);
        issueRepository.delete(issue);
    }

    @Transactional
    public void modifyIssue(ModifyIssueRequestDto dto) {
        Issue issue = issueRepository.findById(dto.getIssueId()).orElseThrow(NotExistsSprintException::new);
        issue.modify(dto.getTitle(),dto.getContent());
        issueValidator.validate(issue, dto.getWriterId());
    }
}
