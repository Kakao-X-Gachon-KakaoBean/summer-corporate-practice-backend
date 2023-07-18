package com.kakaobean.core.releasenote.infrastructure;

import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.ManuscriptQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.kakaobean.core.member.domain.QMember.member;
import static com.kakaobean.core.releasenote.domain.QManuscript.*;

@Repository
@RequiredArgsConstructor
public class ManuscriptQueryRepositoryImpl implements ManuscriptQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FindManuscriptResponseDto> findById (Long manuscriptId) {
        FindManuscriptResponseDto responseDto = queryFactory
                .select(
                        Projections.constructor(
                                FindManuscriptResponseDto.class,
                                member.name,
                                manuscript.id,
                                manuscript.title,
                                manuscript.content,
                                manuscript.version
                        )
                )
                .from(manuscript)
                .join(member).on(manuscript.lastEditedMemberId.eq(member.id))
                .where(manuscript.id.eq(manuscriptId))
                .fetchFirst();
        return Optional.ofNullable(responseDto);
    }
}
