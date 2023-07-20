package com.kakaobean.core.releasenote.infrastructure;


import com.kakaobean.common.PagingUtils;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptsResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.ManuscriptQueryRepository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kakaobean.core.member.domain.QMember.member;
import static com.kakaobean.core.releasenote.domain.QManuscript.*;

@Repository
@RequiredArgsConstructor
public class ManuscriptQueryRepositoryImpl implements ManuscriptQueryRepository {

    private static final int PAGE_SIZE = 10;
    private static final String PAGING_STANDARD = "version";

    private final JPAQueryFactory queryFactory;

    @Override
    public FindManuscriptsResponseDto findByProjectId (Long projectId, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, PAGING_STANDARD));
        List<FindManuscriptsResponseDto.ManuscriptDto> result = queryFactory
                .select(
                        Projections.constructor(
                                FindManuscriptsResponseDto.ManuscriptDto.class,
                                manuscript.id,
                                manuscript.title,
                                manuscript.version
                        )
                )
                .from(manuscript)
                .where(manuscript.projectId.eq(projectId))
                .offset(pageable.getOffset())
                .limit(PAGE_SIZE + 1) // 페이징을 위해 1을 더 가져온다.
                .fetch();

        if(result.size() > PAGE_SIZE){
            return new FindManuscriptsResponseDto(false, PagingUtils.applyPaging(result));
        }
        return new FindManuscriptsResponseDto(true, result);
    }

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
