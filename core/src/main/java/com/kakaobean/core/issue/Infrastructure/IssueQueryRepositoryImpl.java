package com.kakaobean.core.issue.Infrastructure;

import com.kakaobean.common.PagingUtils;
import com.kakaobean.core.issue.domain.repository.query.FindIndividualIssueResponseDto;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.issue.domain.repository.query.IssueQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakaobean.core.issue.domain.QComment.comment;
import static com.kakaobean.core.issue.domain.QIssue.issue;
import static com.kakaobean.core.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class IssueQueryRepositoryImpl implements IssueQueryRepository {
    private static final int PAGE_SIZE = 8;

    private static final String PAGING_STANDARD = "updatedAt";

    private final JPAQueryFactory queryFactory;

    @Override
    public FindIssuesWithinPageResponseDto findByProjectId(Long projectId, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, PAGING_STANDARD));
        List<FindIssuesWithinPageResponseDto.IssueDto> result = queryFactory
                .select(
                        Projections.constructor(
                                FindIssuesWithinPageResponseDto.IssueDto.class,
                                issue.id,
                                issue.title,
                                issue.writerId,
                                member.name,
                                issue.updatedAt
                        )
                )
                .from(issue)
                .leftJoin(member)
                .on(issue.writerId.eq(member.id))
                .where(issue.projectId.eq(projectId))
                .offset(pageable.getOffset())
                .limit(PAGE_SIZE + 1) // 페이징을 위해 1을 더 가져온다.
                .fetch();

        if(result.size() > PAGE_SIZE){
            return new FindIssuesWithinPageResponseDto(false, PagingUtils.applyPaging(result));
        }
        return new FindIssuesWithinPageResponseDto(true, result);
    }

    @Override
    public FindIndividualIssueResponseDto findByIssueId(Long issueId){
        FindIndividualIssueResponseDto result = queryFactory
                .select(
                        Projections.constructor(
                                FindIndividualIssueResponseDto.class,
                                issue.id,
                                issue.title,
                                issue.content,
                                issue.updatedAt,
                                member.name,
                                member.thumbnailImg
                                )
                )
                .from(issue)
                .leftJoin(member)
                .on(issue.writerId.eq(member.id))
                .where(issue.id.eq(issueId))
                .fetchFirst();
        result.getComments().addAll(findCommentByIssueId(issueId));

        return result;
    }

    private List<FindIndividualIssueResponseDto.CommentDto> findCommentByIssueId(Long issueId){
        return queryFactory
                .select(
                        Projections.constructor(
                                FindIndividualIssueResponseDto.CommentDto.class,
                                comment.id,
                                comment.content,
                                comment.updatedAt,
                                member.name,
                                member.thumbnailImg
                                )
                )
                .from(comment)
                .leftJoin(member)
                .on(comment.writerId.eq(member.id))
                .where(comment.issueId.eq(issueId))
                .fetch();
    }
}
