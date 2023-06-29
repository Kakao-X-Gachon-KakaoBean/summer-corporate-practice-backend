package com.kakaobean.independentlysystem.email.dto;

import com.amazonaws.services.simpleemail.model.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SesServiceRequest {

    private String from;
    private List<String> to;
    private String subject;
    private String content;

    @Builder
    public SesServiceRequest(String from, List<String> to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public SendEmailRequest toSendRequestDto(){
        Destination destination = new Destination().withToAddresses(this.to);

        Message message = new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body().withHtml(createContent(this.content)));

        return new SendEmailRequest()
                .withSource(this.from)
                .withDestination(destination)
                .withMessage(message);
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }
}
