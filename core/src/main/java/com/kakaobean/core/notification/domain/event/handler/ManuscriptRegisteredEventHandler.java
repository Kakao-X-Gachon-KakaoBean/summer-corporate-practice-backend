package com.kakaobean.core.notification.domain.event.handler;

import com.kakaobean.core.notification.domain.event.SendRegisterManuscriptNotificationEvent;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.releasenote.domain.event.ManuscriptRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ManuscriptRegisteredEventHandler {

    private final SendMessageNotificationService sendMessageNotificationService;
    private final ProjectRepository projectRepository;

    @TransactionalEventListener(ManuscriptRegisteredEvent.class)
    public void handle(ManuscriptRegisteredEvent event) {
        if(event != null){
            Project project = projectRepository.findById(event.getProjectId())
                    .orElseThrow(NotExistsProjectException::new);

            sendMessageNotificationService.sendMessage(
                    new SendRegisterManuscriptNotificationEvent(
                            event.getProjectId(),
                            project.getTitle(),
                            event.getTitle(),
                            event.getManuscriptId(),
                            event.getVersion()
                    )
            );
        }
    }


}
