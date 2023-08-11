package com.kakaobean.core.releasenote.infrastructure.querydsl;


import com.kakaobean.common.PagingUtils;
import com.kakaobean.core.releasenote.domain.repository.query.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.kakaobean.core.releasenote.domain.QReleaseNote.*;

@Repository
@RequiredArgsConstructor
public class ReleaseNoteQueryRepositoryImpl implements ReleaseNoteQueryRepository {

    private static final int PAGE_SIZE = 10;
    private static final String PAGING_STANDARD = "version";

    private final JPAQueryFactory queryFactory;

    @Override
    public FindPagingReleaseNotesResponseDto  findByProjectId(Long projectId, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, PAGING_STANDARD));
        List<FindPagingReleaseNotesResponseDto.ReleaseNoteDto> result = queryFactory
                .select(
                        Projections.constructor(
                                FindPagingReleaseNotesResponseDto.ReleaseNoteDto.class,
                                releaseNote.id,
                                releaseNote.title,
                                releaseNote.version,
                                releaseNote.content
                        )
                )
                .from(releaseNote)
                .where(releaseNote.projectId.eq(projectId))
                .offset(pageable.getOffset())
                .limit(PAGE_SIZE + 1) // 페이징을 위해 1을 더 가져온다.
                .fetch();

        if(result.size() > PAGE_SIZE){
            return new FindPagingReleaseNotesResponseDto(false, PagingUtils.applyPaging(result));
        }
        return new FindPagingReleaseNotesResponseDto(true, result);
    }

    @Override
    public Optional<FindReleaseNoteResponseDto> findById(Long releaseNoteId) {
        FindReleaseNoteResponseDto responseDto = queryFactory
                .select(
                        Projections.constructor(
                                FindReleaseNoteResponseDto.class,
                                releaseNote.id,
                                releaseNote.title,
                                releaseNote.content,
                                releaseNote.version,
                                releaseNote.createdAt
                        )
                )
                .from(releaseNote)
                .where(releaseNote.id.eq(releaseNoteId))
                .fetchFirst();
        return Optional.ofNullable(responseDto);
    }

    @Override
    public FindReleaseNotesResponseDto findAllByProjectId(Long projectId) {
        List<FindReleaseNotesResponseDto.ReleaseNoteDto> result = queryFactory
                .select(
                        Projections.constructor(
                                FindReleaseNotesResponseDto.ReleaseNoteDto.class,
                                releaseNote.id,
                                releaseNote.title,
                                releaseNote.version
                        )
                )
                .from(releaseNote)
                .where(releaseNote.projectId.eq(projectId))
                .fetch();
        return new FindReleaseNotesResponseDto(result);
    }
}
