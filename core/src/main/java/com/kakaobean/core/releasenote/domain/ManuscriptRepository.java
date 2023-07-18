package com.kakaobean.core.releasenote.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManuscriptRepository extends JpaRepository<Manuscript, Long> {

    Optional<Manuscript> findManuscriptByVersion(String version);
}
