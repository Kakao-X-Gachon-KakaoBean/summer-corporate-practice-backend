package com.kakaobean.core.releasenote.domain.repository;

import com.kakaobean.core.releasenote.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Modifying
    @Query("update History h set h.status = 'INACTIVE' where h.status = 'ACTIVE' and h.releaseNoteId = :releaseNoteId")
    void deleteByReleaseNoteId(Long releaseNoteId);
}
