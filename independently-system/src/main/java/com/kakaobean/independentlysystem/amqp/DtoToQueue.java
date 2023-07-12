package com.kakaobean.independentlysystem.amqp;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DtoToQueue {

    private String url;
    private LocalDateTime issueDate;
    private String title;

    public DtoToQueue(String url, LocalDateTime issueDate, String title) {
        this.url = url;
        this.issueDate = issueDate;
        this.title = title;
    }
}
