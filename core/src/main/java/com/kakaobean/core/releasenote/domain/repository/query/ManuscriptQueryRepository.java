package com.kakaobean.core.releasenote.domain.repository.query;

import java.util.Optional;

public interface ManuscriptQueryRepository {
    Optional<FindManuscriptResponseDto> findById (Long manuscriptId);
    FindPagingManuscriptsResponseDto findByProjectIdWithPaging(Long projectId, Integer page);
    FindManuscriptsResponseDto findAllByProjectId(Long projectId);
}
