package com.kakaobean.core.releasenote.domain.repository;

import com.kakaobean.core.releasenote.domain.Manuscript;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ManuscriptRepository extends JpaRepository<Manuscript, Long> {

    Optional<Manuscript> findManuscriptByVersion (String version);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Manuscript m where m.id = :id")
    Optional<Manuscript> findByIdWithPESSIMISTICLock (Long id);

    @Modifying
    @Query("update Manuscript ma set ma.status = 'INACTIVE' where ma.projectId = :projectId and ma.status = 'ACTIVE'")
    void deleteByProjectId(Long projectId);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("update Manuscript ma set ma.status = 'INACTIVE' where ma.version = :version and ma.projectId = :projectId and ma.status = 'ACTIVE'")
    void deleteByVersionAndProjectId(String version, Long projectId);

    List<Manuscript> findManuscriptByProjectId(Long projectId);
}
