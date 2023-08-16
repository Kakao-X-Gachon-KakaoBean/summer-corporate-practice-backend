package com.kakaobean.notification;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationQueryRepository notificationQueryRepository;

    @GetMapping("/notifications")
    public ResponseEntity findNotification(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<FindNotificationResponseDto> response = notificationQueryRepository.findByMemberId(userPrincipal.getId());
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/notifications/page")
    public ResponseEntity findNextNotifications(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @RequestParam(required = false) Long lastNotificationId){
        List<FindNotificationResponseDto> response = notificationQueryRepository.findByPaginationNoOffset(lastNotificationId, userPrincipal.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/notifications/{notificationId}")
    public ResponseEntity modifyNotification(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long notificationId){
        notificationRepository.modifyReadStatus(notificationId);
        return new ResponseEntity(CommandSuccessResponse.from("알람 읽기 상태 변경에 성공했습니다."), OK);
    }
}
