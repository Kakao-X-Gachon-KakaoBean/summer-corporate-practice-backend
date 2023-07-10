package com.kakaobean.core.sprint.domain.repository;

import com.kakaobean.core.sprint.domain.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @Modifying
    @Query("update Sprint s set s.status = 'INACTIVE' where s.status = 'ACTIVE' and s.projectId = :projectId")
    List<Sprint> deleteByProjectId(Long projectId);

}
