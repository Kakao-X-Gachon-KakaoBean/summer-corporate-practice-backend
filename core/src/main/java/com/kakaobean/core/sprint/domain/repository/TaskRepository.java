package com.kakaobean.core.sprint.domain.repository;

import com.kakaobean.core.sprint.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("update Task t set t.status = 'INACTIVE' where t.status = 'ACTIVE' and t.sprintId = :sprintId")
    void deleteBySprintId(Long sprintId);
}
