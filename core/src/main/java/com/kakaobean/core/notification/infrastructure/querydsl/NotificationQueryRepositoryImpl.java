package com.kakaobean.core.notification.infrastructure.querydsl;

import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakaobean.core.notification.domain.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FindNotificationResponseDto> findNotification(Long memberId) {
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
