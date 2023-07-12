package com.kakaobean.core.member.domain.event;

import com.kakaobean.core.common.event.Event;
import lombok.Getter;

@Getter
public class MemberRegisteredEvent extends Event {

    private final Long memberId;

    public MemberRegisteredEvent(Long memberId) {
        this.memberId = memberId;
    }
}
