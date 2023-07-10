package com.kakaobean.core.project.domain.repository;


import com.kakaobean.core.project.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query("select pm from ProjectMember pm where pm.memberId = :memberId and pm.projectId = :projectId and pm.status = 'ACTIVE'")
    Optional<ProjectMember> findByMemberIdAndProjectId(@Param("memberId") Long memberId, @Param("projectId") Long ProjectId);

    @Modifying
    @Query("update ProjectMember pm set pm.status = 'INACTIVE' where pm.status = 'ACTIVE' and pm.projectId = :projectId")
    void deleteByProjectId(Long projectId);
}
