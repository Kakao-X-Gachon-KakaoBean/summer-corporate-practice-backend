package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;

import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.exception.NotExistsReleaseNoteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        List<NotificationSentEvent.NotificationTarget> infos = saveNotifications(releaseNote);
        return new SendDeploymentReleaseNoteNotificationEvent(
                releaseNote.getProjectId(),
                releaseNote.getTitle(),
                infos,
                project.getTitle(),
                releaseNoteId
        );
    }

    private List<NotificationSentEvent.NotificationTarget> saveNotifications(ReleaseNote releaseNote) {
        return projectQueryRepository
                .findProjectMembers(releaseNote.getProjectId())
                .stream()
                .map((dto -> {
                    notificationRepository.save(new Notification(ACTIVE, dto.getProjectMemberId(), releaseNote.getProjectId(), releaseNote.getTitle(), RELEASE_NOTE_DEPLOYMENT, false));
                    return new NotificationSentEvent.NotificationTarget(dto.getProjectMemberEmail(), dto.getProjectMemberId());
                }))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == RELEASE_NOTE_DEPLOYMENT;
    }
}
