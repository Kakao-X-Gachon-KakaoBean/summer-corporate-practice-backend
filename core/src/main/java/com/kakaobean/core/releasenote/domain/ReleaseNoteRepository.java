package com.kakaobean.core.releasenote.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long> {

    @Modifying
    @Query("update ReleaseNote rn set rn.status = 'INACTIVE' where rn.projectId = :projectId and rn.status = 'ACTIVE'")
    List<ReleaseNote> deleteByProjectId(Long projectId);
}
