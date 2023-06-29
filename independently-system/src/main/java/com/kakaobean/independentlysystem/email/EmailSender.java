package com.kakaobean.independentlysystem.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.kakaobean.common.RandomUtils;
import com.kakaobean.independentlysystem.email.dto.SesServiceRequest;
import com.kakaobean.independentlysystem.utils.ValidationEmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.Validation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kakaobean.independentlysystem.utils.ValidationEmailUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    @Value("${aws.ses.sender}")
    private String sender;

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public String sendVerificationEmail(String receiveEmail) {
        String authKey = RandomUtils.creatRandomKey();
        String subject = "[코코노트] 인증 번호 발송 메일입니다.";
        SesServiceRequest emailSenderDto = makeValidationEmailSenderDto(receiveEmail, subject, () -> ValidationEmailUtils.getEmailValidationHtml(authKey));
        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(emailSenderDto.toSendRequestDto());
        confirmSentEmail(sendEmailResult);
        return authKey;
    }

    public void sendProjectInvitationEmail(String receiveEmail,
                                           String projectName,
                                           String projectSecretKey){
        String subject = "[코코노트] 프로젝트 초대 메일입니다.";

    }

    private SesServiceRequest makeValidationEmailSenderDto(String receiveEmail,
                                                           String subject,
                                                           EmailHTMLMaker maker){
        List<String> receiver = List.of(receiveEmail);
        String html = maker.makeEmailHtml();
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
