package com.kakaobean.core.notification.application;

import com.kakaobean.core.notification.application.dto.RegisterNotificationRequestDto;
import com.kakaobean.core.notification.domain.SendEmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationService notificationService;
    private final SendEmailNotificationService sendEmailNotificationService;

    public void registerNotificationByEvent(RegisterNotificationRequestDto dto){

        //알람 저장
        notificationService.registerNotificationByEvent(dto);

        //알람 전송
        //1. mq, 2. 이메일
        sendEmailNotificationService.sendNotificationEmail();
    }
}
