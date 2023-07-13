package com.kakaobean.core.releasenote.domain.repository;


import com.kakaobean.core.releasenote.domain.ReleaseNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long> {

    @Modifying
    @Query("update ReleaseNote rn set rn.status = 'INACTIVE' where rn.projectId = :projectId and rn.status = 'ACTIVE'")
    void deleteByProjectId(Long projectId);

    @Query("select rn from ReleaseNote rn where rn.projectId = :projectId")
    List<ReleaseNote> findByProjectId(Long projectId);
}
