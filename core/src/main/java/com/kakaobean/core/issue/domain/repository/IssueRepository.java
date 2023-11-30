package com.kakaobean.core.issue.domain.repository;

import com.kakaobean.core.issue.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Modifying
    @Query("update Issue i set i.status = 'INACTIVE' where i.projectId = :projectId and i.status='ACTIVE'")
    void deleteByProjectId(Long projectId);


}
