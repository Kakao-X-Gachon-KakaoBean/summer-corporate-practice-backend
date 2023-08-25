package com.kakaobean.independentlysystem.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.kakaobean.independentlysystem.email.dto.SesServiceRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    @Value("${aws.ses.sender}")
    private String sender;

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public void sendEmail(List<String> receiver, String subject, EmailHTMLMaker maker) {
//        String html = maker.makeEmailHtml();
//        SesServiceRequest emailRequest = makeEmailRequest(receiver, subject, html);
//        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(emailRequest.toSendRequestDto());
//        confirmSentEmail(sendEmailResult);
    }

    private SesServiceRequest makeEmailRequest(List<String> receiver, String subject, String html) {
        return SesServiceRequest.builder()
                .from(sender)
                .to(receiver)
                .subject(subject)
                .content(html)
                .build();
    }

    private void confirmSentEmail(SendEmailResult sendEmailResult) {
        if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            log.error("email 서버 요청을 실패했습니다.");
            throw new RuntimeException("에러 요청을 실패했습니다.");
        }
    }
}
