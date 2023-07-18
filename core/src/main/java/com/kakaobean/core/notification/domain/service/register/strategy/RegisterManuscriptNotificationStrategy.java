package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.RegisterManuscriptNotificationEvent;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptRepository;
import com.kakaobean.core.releasenote.exception.NotExistsManuscriptException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.notification.domain.NotificationType.REGISTER_MANUSCRIPT;

@Component
@RequiredArgsConstructor
public class RegisterManuscriptNotificationStrategy implements RegisterNotificationStrategy {

    private final ManuscriptRepository manuscriptRepository;
    private final NotificationRepository notificationRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public NotificationSentEvent register(Long manuScriptId) {
        Manuscript manuscript = manuscriptRepository.findById(manuScriptId)
                .orElseThrow(NotExistsManuscriptException::new);
        Project project = projectRepository.findById(manuscript.getProjectId())
                .orElseThrow(NotExistsProjectException::new);

        String url = "/projects/" + manuscript.getProjectId() + "/manuscripts/" + manuscript.getId();
        String content = manuscript.getTitle() + " 원고가 생성되었습니다.";
        saveNotifications(manuscript, url);

        return new RegisterManuscriptNotificationEvent(url, project.getTitle(), content, LocalDateTime.now(), project.getId());
    }

    private List<String> saveNotifications(Manuscript manuscript, String url) {
        return projectQueryRepository
                .findProjectMembers(manuscript.getProjectId())
                .stream()
                .map((dto -> {
                    notificationRepository.save(new Notification(ACTIVE, dto.getProjectMemberId(), url, false));
                    return dto.getProjectMemberEmail();
                }))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == REGISTER_MANUSCRIPT;
    }
}
