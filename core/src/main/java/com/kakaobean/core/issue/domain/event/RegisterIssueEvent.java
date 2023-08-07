package com.kakaobean.core.issue.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class RegisterIssueEvent extends Event {

    private Long issueId;

    public RegisterIssueEvent(Long issueId) {
        this.issueId = issueId;
    }
}