package com.kakaobean.notification;

import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.FindPagingNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

// 읽음 처리 patch api
// 만약에 테스크에 할당받아서 알람이 저장되었는데, 할당이 해제되었을 때 그 알람을 누르면?
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryRepository notificationQueryRepository;

    @GetMapping("/notifications")
    public ResponseEntity findNotification(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<FindNotificationResponseDto> response = notificationQueryRepository.findByMemberId(userPrincipal.getId());
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/notifications/page")
    public ResponseEntity findnotifications(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Integer page){
        FindPagingNotificationResponseDto response = notificationQueryRepository.findByMemberIdWithPaging(userPrincipal.getId(), page);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
