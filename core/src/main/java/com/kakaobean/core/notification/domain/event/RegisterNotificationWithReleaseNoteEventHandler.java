package com.kakaobean.core.notification.domain.event;

import com.kakaobean.core.notification.application.NotificationFacade;
import com.kakaobean.core.notification.application.dto.RegisterNotificationRequestDto;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RegisterNotificationWithReleaseNoteEventHandler {

    private final NotificationFacade notificationFacade;

    @Async
    @TransactionalEventListener(value = ReleaseNoteRegisteredEvent.class)
    public void handle(ReleaseNoteRegisteredEvent event){
        if(event != null){
            notificationFacade.registerNotificationByEvent(RegisterNotificationRequestDto.from(event));
        }
    }
}
