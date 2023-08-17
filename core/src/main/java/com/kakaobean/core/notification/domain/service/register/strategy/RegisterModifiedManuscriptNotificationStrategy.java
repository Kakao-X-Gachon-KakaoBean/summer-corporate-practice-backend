package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.ModifiedManuscriptNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.utils.NotificationUtils;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.query.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.exception.NotExistsManuscriptException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class RegisterModifiedManuscriptNotificationStrategy implements RegisterNotificationStrategy {

    private final ManuscriptRepository manuscriptRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectRepository projectRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationSentEvent register(Long manuscriptId) {
        Manuscript manuscript = manuscriptRepository.findById(manuscriptId)
                .orElseThrow(NotExistsManuscriptException::new);
        Project project = projectRepository.findById(manuscript.getProjectId())
                .orElseThrow(NotExistsProjectException::new);

        String url = "/projects/" + manuscript.getProjectId() + "/manuscripts/" + manuscript.getId() ;
        String content = manuscript.getTitle() + " 원고 수정이 끝났습니다.";
        String finalContent = NotificationUtils.makeNotificationContent(project.getTitle(), content);
        List<Notification> notifications = makeNotifications(manuscript, url, finalContent);
        notificationRepository.saveAll(notifications);
        return new ModifiedManuscriptNotificationEvent(url, project.getTitle(), finalContent, LocalDateTime.now(), project.getId());
    }

    private List<Notification> makeNotifications(Manuscript manuscript, String url, String content) {
        return projectQueryRepository
                .findProjectMembers(manuscript.getProjectId())
                .stream()
                .map(memberInfo -> new Notification(ACTIVE, memberInfo.getProjectMemberId(), url, false, content))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == MODIFIED_MANUSCRIPT;
    }
}
