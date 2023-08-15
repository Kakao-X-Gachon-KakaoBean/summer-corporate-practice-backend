package com.kakaobean.notification;

import com.kakaobean.core.notification.domain.repository.query.FindNotificationResponseDto;
import com.kakaobean.core.notification.domain.repository.query.NotificationQueryRepository;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

// 최근 5개 알람 리턴해주기
// 읽음 필드 값도 내려주기, 읽음처리 어케할지 생각해봐야함
// 만약에 테스크에 할당받아서 알람이 저장되었는데, 할당이 해제되었을 때 그 알람을 누르면?
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryRepository notificationQueryRepository;

    @GetMapping("/notifications")
    public ResponseEntity findNotification(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<FindNotificationResponseDto> response = notificationQueryRepository.findNotification(userPrincipal.getId());
        return new ResponseEntity(response, OK);
    }

}
