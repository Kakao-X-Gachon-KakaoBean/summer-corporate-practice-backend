package com.kakaobean.core.sprint.domain.repository;

import com.kakaobean.core.sprint.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Transactional
    @Modifying
    @Query("update Task t set t.status = 'INACTIVE' where t.sprintId = :sprintId and t.status = 'ACTIVE'")
    void deleteBySprintId(Long sprintId);
}
