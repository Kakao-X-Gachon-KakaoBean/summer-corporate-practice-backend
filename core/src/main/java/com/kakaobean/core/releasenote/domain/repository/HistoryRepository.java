package com.kakaobean.core.releasenote.domain.repository;

import com.kakaobean.core.releasenote.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Transactional
    @Modifying
    @Query("update History h set h.status = 'INACTIVE' where h.releaseNoteId = :releaseNoteId and h.status = 'ACTIVE'")
    void deleteByReleaseNoteId(Long releaseNoteId);
}
