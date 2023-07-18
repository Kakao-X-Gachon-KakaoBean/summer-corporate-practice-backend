package com.kakaobean.independentlysystem.amqp;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DtoToQueue {

    private String url;
    private LocalDateTime issueDate;
    private String projectTitle;
    private String title;

    public DtoToQueue(String url, LocalDateTime issueDate, String projectTitle, String title) {
        this.url = url;
        this.issueDate = issueDate;
        this.projectTitle = projectTitle;
        this.title = title;
    }
}
