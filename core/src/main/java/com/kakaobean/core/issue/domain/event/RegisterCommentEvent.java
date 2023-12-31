package com.kakaobean.core.issue.domain.event;

import com.kakaobean.core.common.event.Event;
import com.kakaobean.core.issue.domain.Issue;
import lombok.Getter;

@Getter
public class RegisterCommentEvent extends Event {

    private Long commentId;

    public RegisterCommentEvent(Long commentId) {
        this.commentId = commentId;
    }
}
