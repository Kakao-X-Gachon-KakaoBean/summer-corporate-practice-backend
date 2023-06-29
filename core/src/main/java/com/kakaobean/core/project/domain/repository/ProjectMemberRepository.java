package com.kakaobean.core.project.domain.repository;


import com.kakaobean.core.project.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}
