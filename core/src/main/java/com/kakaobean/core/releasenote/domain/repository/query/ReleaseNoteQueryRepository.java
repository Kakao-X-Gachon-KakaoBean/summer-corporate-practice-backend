package com.kakaobean.core.releasenote.domain.repository.query;

import java.util.Optional;

public interface ReleaseNoteQueryRepository {
    FindReleaseNotesResponseDto findByProjectId(Long projectId, Integer page);
    Optional<FindReleaseNoteResponseDto> findById(Long releaseNoteId);
}
