package com.kakaobean.independentlysystem.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.kakaobean.independentlysystem.email.dto.SesServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kakaobean.independentlysystem.config.ses.AwsSesUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    @Value("${aws.ses.sender}")
    private String sender;

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public String  sendVerificationEmail(String receiveEmail) {
        String authKey = createAuthKey();
        SesServiceRequest emailSenderDto = makeEmailSenderDto(receiveEmail, authKey);
        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(emailSenderDto.toSendRequestDto());
        confirmSentEmail(sendEmailResult);
        return authKey;
    }

    private SesServiceRequest makeEmailSenderDto(String receiveEmail, String authKey) {
        List<String> receiver = List.of(receiveEmail);
        String subject = getSubject();
        String emailVerificationHtml = getEmailVerificationHtml(authKey);

        return SesServiceRequest.builder()
                .from(sender)
                .to(receiver)
                .subject(subject)
                .content(emailVerificationHtml)
                .build();
    }

    private void confirmSentEmail(SendEmailResult sendEmailResult) {
        if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            log.error("email 서버 요청을 실패했습니다.");
            throw new RuntimeException("에러 요청을 실패했습니다.");
        }
    }
}
