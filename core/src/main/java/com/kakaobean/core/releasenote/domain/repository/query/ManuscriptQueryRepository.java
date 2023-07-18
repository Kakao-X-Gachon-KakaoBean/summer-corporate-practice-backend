package com.kakaobean.core.releasenote.domain.repository.query;

import java.util.Optional;

public interface ManuscriptQueryRepository {
    Optional<FindManuscriptResponseDto> findById (Long manuscriptId);
    FindManuscriptsResponseDto findByProjectId(Long projectId, Integer page);
}
