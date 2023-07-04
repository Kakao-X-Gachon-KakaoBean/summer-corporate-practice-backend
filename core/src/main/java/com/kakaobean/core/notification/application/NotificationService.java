package com.kakaobean.core.notification.application;

import com.kakaobean.core.notification.application.dto.RegisterNotificationRequestDto;
import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.notification.application.dto.RegisterNotificationRequestDto.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerNotificationByEvent(RegisterNotificationRequestDto dto){
        for (NotifiedTargetInfo info : dto.getNotifiedInfos()) {
            Notification notification = new Notification(ACTIVE, info.getMemberId(), dto.getSourceId(), dto.getTitle(), dto.getType(), false);
            notificationRepository.save(notification);
        }
    }
}
