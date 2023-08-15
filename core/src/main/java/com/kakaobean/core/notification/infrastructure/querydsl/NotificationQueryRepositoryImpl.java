package com.kakaobean.core.notification.infrastructure.querydsl;

import com.kakaobean.common.PagingUtils;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.FindPagingNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    private final JPAQueryFactory queryFactory;

    // 조회했던 마지막 notification id를 받는다
    @Override
    public List<FindNotificationResponseDto> findByPaginationNoOffset(Long notificationId, Long memberId) {
        return queryFactory
                .select(Projections.constructor(FindNotificationResponseDto.class,
                                notification.createdAt,
                                notification.content,
                                notification.url,
                                notification.hasRead
                        )
                )
                .from(notification)
                .where(
                        ltNotificationId(notificationId),
                        notification.memberId.eq(memberId)
                )
                .orderBy(notification.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression ltNotificationId(Long notificationId) {
        if (notificationId == null) {
            return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
        }

        return notification.id.lt(notificationId);
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
