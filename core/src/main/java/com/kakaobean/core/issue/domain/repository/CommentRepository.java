package com.kakaobean.core.issue.domain.repository;

import com.kakaobean.core.issue.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("update Comment i set i.status = 'INACTIVE' where i.issueId = :issueId and i.status='ACTIVE'")
    void deleteByIssueId(Long issueId);
}
