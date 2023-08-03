package com.kakaobean.core.issue.domain.repository;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.sprint.domain.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Modifying
    @Query("update Issue i set i.status = 'INACTIVE' where i.projectId = :projectId and i.status='ACTIVE'")
    void deleteByProjectId(Long projectId);

    @Query("select i from Issue i where i.id = :issueId")
    Optional<Issue> findByIssueId(Long issueId);
}
