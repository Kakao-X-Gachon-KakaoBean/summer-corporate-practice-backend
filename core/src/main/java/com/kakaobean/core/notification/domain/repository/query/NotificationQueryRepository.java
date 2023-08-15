package com.kakaobean.core.notification.domain.repository.query;

import java.util.List;

public interface NotificationQueryRepository {
    List<FindNotificationResponseDto> findByMemberId(Long memberId);
    FindPagingNotificationResponseDto findByMemberIdWithPaging(Long memberId, Integer page);
}
