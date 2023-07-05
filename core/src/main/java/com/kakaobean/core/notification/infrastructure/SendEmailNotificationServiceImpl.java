package com.kakaobean.core.notification.infrastructure;

import com.kakaobean.core.notification.domain.SendEmailNotificationService;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmailNotificationServiceImpl implements SendEmailNotificationService {

    private final EmailSender emailSender;

    public SendEmailNotificationServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendNotificationEmail() {
        //emailSender.sendNotificationEmail();
    }
}
