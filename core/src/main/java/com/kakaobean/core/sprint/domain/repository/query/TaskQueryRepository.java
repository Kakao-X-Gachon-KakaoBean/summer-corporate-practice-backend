package com.kakaobean.core.sprint.domain.repository.query;

public interface TaskQueryRepository {
    FindTaskResponseDto findTask(Long taskId);
}
