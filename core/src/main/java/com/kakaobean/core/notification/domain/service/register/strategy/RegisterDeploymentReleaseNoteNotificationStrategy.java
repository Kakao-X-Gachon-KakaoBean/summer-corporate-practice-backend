package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;

import com.kakaobean.core.notification.domain.event.DeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.utils.NotificationUtils;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.query.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.exception.NotExistsReleaseNoteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class RegisterDeploymentReleaseNoteNotificationStrategy implements RegisterNotificationStrategy {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final NotificationRepository notificationRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public NotificationSentEvent register(Long releaseNoteId) {
        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NotExistsReleaseNoteException::new);
        Project project = projectRepository.findById(releaseNote.getProjectId())
                .orElseThrow(NotExistsProjectException::new);

        String url = "/projects/" + releaseNote.getProjectId() + "/release-notes/" + releaseNote.getId();
        String content = releaseNote.getTitle() + "이 배포되었습니다.";
        String finalContent = NotificationUtils.makeNotificationContent(project.getTitle(), content);
        List<String> emails = saveNotifications(releaseNote, url, finalContent);
        return new DeploymentReleaseNoteNotificationEvent(url, project.getTitle(), content, LocalDateTime.now(), emails, project.getId());
    }

    private List<String> saveNotifications(ReleaseNote releaseNote, String url, String finalContent) {
        return projectQueryRepository
                .findProjectMembers(releaseNote.getProjectId())
                .stream()
                .map((dto -> {
                    notificationRepository.save(new Notification(ACTIVE, dto.getProjectMemberId(), url, false, finalContent));
                    return dto.getProjectMemberEmail();
                }))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == RELEASE_NOTE_DEPLOYMENT;
    }
}
