package com.kakaobean.fixture.notification;

import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationsResponseDto;

import java.util.List;

public class FindNotificationResponseDtoFactory {

    private FindNotificationResponseDtoFactory() {
    }

    public static FindNotificationsResponseDto create() {
        List<FindNotificationResponseDto> result = List.of(
                new FindNotificationResponseDto(
                        10L,
                        "23. 8. 16. 오전 3:07",
                        "[ 코코노트 ] 코코노트 프로젝트의 권한이 VIEWER 으로 변경되었습니다.",
                        "/projects/1",
                        false
                ),
                new FindNotificationResponseDto(
                        20L,
                        "23. 8. 16. 오전 3:07",
                        "[ 코코아 ] 코코아 작업이 할당되었습니다.",
                        "/projects/1/sprints/1/1",
                        false
                )
        );
        return new FindNotificationsResponseDto(result);
    }
}
