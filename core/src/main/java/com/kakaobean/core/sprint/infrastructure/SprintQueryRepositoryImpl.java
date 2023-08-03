package com.kakaobean.core.sprint.infrastructure;

import com.kakaobean.core.sprint.domain.repository.query.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakaobean.core.member.domain.QMember.member;
import static com.kakaobean.core.sprint.domain.QSprint.sprint;
import static com.kakaobean.core.sprint.domain.QTask.task;

@Repository
@RequiredArgsConstructor
public class SprintQueryRepositoryImpl implements SprintQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 프로젝트 id에 해당하는 모든 스프린트 Dto를 생성한다.
     */
    @Override
    public FindAllSprintResponseDto findAllByProjectId(Long projectId) {

        List<SprintsDto> sprintsDto = queryFactory
                .select(
                        Projections.constructor(
                                SprintsDto.class,
                                sprint.id,
                                sprint.title,
                                sprint.startDate,
                                sprint.endDate
                        )
                )
                .from(sprint)
                .where(sprint.projectId.eq(projectId))
                .fetch();

        return new FindAllSprintResponseDto(findAllTasksBySprintId(sprintsDto));
    }

    /**
     * 각 스프린트의 대한 테스크 Dto를 생성한다.
     */
    private List<SprintsDto> findAllTasksBySprintId(List<SprintsDto> sprintsDto) {

        for (SprintsDto dto : sprintsDto) {

            List<TasksDto> result = queryFactory
                    .select(
                            Projections.constructor(
                                    TasksDto.class,
                                    task.id,
                                    task.title
                            )
                    )
                    .from(task)
                    .where(task.sprintId.eq(dto.getSprintId()))
                    .fetch();

            dto.getChildren().addAll(result);
        }

        return sprintsDto;
    }

    /**
     * 스프린트 개별 조회
     */
    @Override
    public FindSprintResponseDto findSprintById(Long sprintId) {

        FindSprintResponseDto result = queryFactory
                .select(
                        Projections.constructor(
                                FindSprintResponseDto.class,
                                sprint.title,
                                sprint.description,
                                sprint.startDate,
                                sprint.endDate
                        )
                )
                .from(sprint)
                .where(sprint.id.eq(sprintId))
                .fetchFirst();

        result.getTasks().addAll(findTaskBySprintId(sprintId));

        return result;
    }

    private List<FindSprintResponseDto.TaskDto> findTaskBySprintId(Long sprintId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                FindSprintResponseDto.TaskDto.class,
                                task.title,
                                task.workStatus,
                                task.workerId,
                                member.name,
                                member.thumbnailImg
                        )
                )
                .from(task)
                .join(member)
                .on(task.workerId.eq(member.id))
                .where(task.sprintId.eq(sprintId))
                .fetch();
    }

}