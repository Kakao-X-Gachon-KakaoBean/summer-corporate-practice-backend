package com.kakaobean.core.project.domain.repository;

import com.kakaobean.core.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select p from Project p where p.status = 'ACTIVE' and p.id = :id")
    Optional<Project> findProjectById(Long id);

    @Query("select p from Project p where p.status = 'ACTIVE' and p.secretKey = :secretKey")
    Optional<Project> findBySecretKey(@Param("secretKey") String secretKey);
}
