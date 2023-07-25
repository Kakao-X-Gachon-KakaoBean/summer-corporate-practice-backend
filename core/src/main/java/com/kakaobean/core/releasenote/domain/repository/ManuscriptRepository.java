package com.kakaobean.core.releasenote.domain.repository;

import com.kakaobean.core.releasenote.domain.Manuscript;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ManuscriptRepository extends JpaRepository<Manuscript, Long> {

    Optional<Manuscript> findManuscriptByVersion (String version);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Manuscript m where m.id = :id")
    Optional<Manuscript> findByIdWithPESSIMISTICLock (Long id);

    @Modifying
    @Query("update Manuscript ma set ma.status = 'INACTIVE' where ma.projectId = :projectId and ma.status = 'ACTIVE'")
    void deleteByProjectId(Long projectId);
}
