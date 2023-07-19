package com.kakaobean.core.issue.application;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    @Transactional(readOnly = false)
    public RegisterIssueResponseDto registerIssue(RegisterIssueRequestDto dto) {
        Issue savedIssue = issueRepository.save(dto.toEntity());
        return new RegisterIssueResponseDto(savedIssue.getId());
    }
}
