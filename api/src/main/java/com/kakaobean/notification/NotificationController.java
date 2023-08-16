package com.kakaobean.notification;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Timed("api.notification")
@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationQueryRepository notificationQueryRepository;

    @GetMapping("/notifications")
    public ResponseEntity findNotification(@AuthenticationPrincipal UserPrincipal userPrincipal){
        log.info("최근 알림 조회 api 시작");
        List<FindNotificationResponseDto> response = notificationQueryRepository.findByMemberId(userPrincipal.getId());
        log.info("최근 알림 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/notifications/page")
    public ResponseEntity findNextNotifications(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @RequestParam(required = false) Long lastNotificationId){
        log.info("알림 페이징 조회 api 시작");
        List<FindNotificationResponseDto> response = notificationQueryRepository.findByPaginationNoOffset(lastNotificationId, userPrincipal.getId());
        log.info("알림 페이징 조회 api 종료");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/notifications/{notificationId}")
    public ResponseEntity modifyNotification(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long notificationId){
        log.info("알림 열람 상태 변경 api 시작");
        notificationRepository.modifyReadStatus(notificationId);
        log.info("알림 열람 상태 변경 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("알람 열람 상태 변경에 성공했습니다."), OK);
    }
}
