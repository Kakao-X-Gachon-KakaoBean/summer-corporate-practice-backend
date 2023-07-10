package com.kakaobean.core.issue.domain.repository;

import com.kakaobean.core.issue.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Modifying
    @Query("update Issue i set i.status = 'INACTIVE' where i.status='ACTIVE' and i.projectId = :projectId")
    void deleteByProjectId(Long projectId);
}
