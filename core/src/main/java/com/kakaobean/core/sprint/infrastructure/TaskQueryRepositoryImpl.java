package com.kakaobean.core.sprint.infrastructure;

import com.kakaobean.core.sprint.domain.repository.query.FindTaskResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.TaskQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.kakaobean.core.member.domain.QMember.member;
import static com.kakaobean.core.sprint.domain.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public FindTaskResponseDto findTask(Long taskId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                FindTaskResponseDto.class,
                                task.title,
                                task.content,
                                task.workStatus,
                                task.workerId,
                                member.name,
                                member.thumbnailImg
                        )
                )
                .from(task)
                .join(member)
                .on(task.workerId.eq(member.id))
                .where(task.id.eq(taskId))
                .fetchFirst();
    }
}
