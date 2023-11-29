package com.kakaobean.core.notification.domain.repository.query;


public interface NotificationQueryRepository {
    FindNotificationsResponseDto findByMemberId(Long memberId);
    FindNotificationsResponseDto findByPaginationNoOffset(Long notificationId, Long memberId);
}
