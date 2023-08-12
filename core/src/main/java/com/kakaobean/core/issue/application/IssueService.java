package com.kakaobean.core.issue.application;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.IssueAccessException;
import com.kakaobean.core.issue.exception.NotExistsIssueException;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private final CommentRepository commentRepository;

    private final ProjectMemberRepository projectMemberRepository;

    @Transactional(readOnly = false)
    public void registerIssue(RegisterIssueRequestDto dto) {
        Issue issue = dto.toEntity();
        projectMemberRepository.findByMemberIdAndProjectId(dto.getWriterId(), dto.getProjectId()).
                orElseThrow(NotExistsProjectMemberException::new);
        issueRepository.save(issue);
        //알림 필요 없으면 그냥 위 두 줄 합쳐도 됨.
    }

    @Transactional
    public void removeIssue(Long memberId, Long issueId){
        Issue issue = issueRepository.findById(issueId).orElseThrow(NotExistsIssueException::new);

        //member + 프로젝트 먼저 검증
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, issue.getProjectId()).orElseThrow(NotExistsProjectMemberException::new);

        //작성자인지 검증
        if(projectMember.getMemberId() != issue.getWriterId()){ throw new IssueAccessException(); }

        commentRepository.deleteByIssueId(issueId);
        issueRepository.delete(issue);
    }
}
