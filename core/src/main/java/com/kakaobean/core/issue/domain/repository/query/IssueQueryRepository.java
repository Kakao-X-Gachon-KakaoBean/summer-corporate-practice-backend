package com.kakaobean.core.issue.domain.repository.query;

public interface IssueQueryRepository {
    FindIssuesWithinPageResponseDto findByProjectId(Long projectId, Integer page);
}
