package com.kakaobean.core.notification.infrastructure.querydsl;

import com.kakaobean.common.PagingUtils;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.FindPagingNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakaobean.core.notification.domain.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private static final int PAGE_SIZE = 10;
    private static final String PAGING_STANDARD = "createdAt";

    private final JPAQueryFactory queryFactory;

    @Override
    public FindPagingNotificationResponseDto findByMemberIdWithPaging (Long memberId, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, PAGING_STANDARD));
        List<FindNotificationResponseDto> result = queryFactory
                .select(
                        Projections.constructor(
                                FindNotificationResponseDto.class,
                                notification.createdAt,
                                notification.content,
                                notification.url,
                                notification.hasRead
                        )
                )
                .from(notification)
                .where(notification.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(PAGE_SIZE + 1) // 페이징을 위해 1을 더 가져온다.
                .fetch();

        if(result.size() > PAGE_SIZE){
            return new FindPagingNotificationResponseDto(false, PagingUtils.applyPaging(result));
        }
        return new FindPagingNotificationResponseDto(true, result);
    }

    @Override
    public List<FindNotificationResponseDto> findByMemberId(Long memberId) {
        return queryFactory.select(
                        Projections.constructor(
                                FindNotificationResponseDto.class,
                                notification.createdAt,
                                notification.content,
                                notification.url,
                                notification.hasRead
                        ))
                .from(notification)
                .where(
                        notification.memberId.eq(memberId)
                )
                .orderBy(notification.createdAt.desc())
                .limit(5) // 최근 알림 5개
                .fetch();
    }
}
