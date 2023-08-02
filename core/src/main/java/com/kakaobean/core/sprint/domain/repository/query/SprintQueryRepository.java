package com.kakaobean.core.sprint.domain.repository.query;

public interface SprintQueryRepository {

    FindAllSprintResponseDto findAllByProjectId(Long sprintId);
}
