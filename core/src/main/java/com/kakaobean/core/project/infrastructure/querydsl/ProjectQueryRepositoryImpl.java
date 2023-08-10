package com.kakaobean.core.project.infrastructure.querydsl;

import com.kakaobean.core.project.application.dto.response.FindProjectInfoResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectTitleResponseDto;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.member.domain.QMember.*;
import static com.kakaobean.core.project.domain.QProject.*;
import static com.kakaobean.core.project.domain.QProjectMember.*;

@Repository
public class ProjectQueryRepositoryImpl implements ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<FindProjectMemberResponseDto> findProjectMembers(Long projectId) {
        return queryFactory.select(
                        Projections.constructor(
                                FindProjectMemberResponseDto.class,
                                member.id,
                                member.name,
                                member.auth.email,
                                projectMember.projectRole,
                                member.thumbnailImg
                        ))
                .from(projectMember)
                .join(member).on(member.id.eq(projectMember.memberId))
                .where(
                        projectMember.status.eq(ACTIVE),
                        projectMember.projectId.eq(projectId)
                )
                .fetch();
    }

    @Override
    public List<FindProjectResponseDto> findProjects(Long memberId) {
        return queryFactory.select(
                        Projections.constructor(
                                FindProjectResponseDto.class,
                                project.id,
                                project.title,
                                project.content
                        ))
                .from(project)
                .join(projectMember).on(projectMember.id.eq(project.id))
                .where(
                        project.status.eq(ACTIVE),
                        projectMember.memberId.eq(memberId)
                )
                .fetch();
    }

    @Override
    public FindProjectInfoResponseDto findProject(Long projectId) {
        FindProjectInfoResponseDto result = queryFactory.select(
                        Projections.constructor(
                                FindProjectInfoResponseDto.class,
                                project.title,
                                project.content
                        )
                )
                .from(project)
                .where(project.id.eq(projectId))
                .fetchFirst();

        result.getProjectMembers().addAll(findProjectMembers(projectId));

        return result;
    }

    @Override
    public FindProjectTitleResponseDto findBySecretKey(String projectSecretKey) {

        return queryFactory.select(
                        Projections.constructor(
                                FindProjectTitleResponseDto.class,
                                project.title
                        )
                )
                .from(project)
                .where(project.secretKey.eq(projectSecretKey))
                .fetchFirst();
    }
}
