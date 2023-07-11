package com.kakaobean.core.notification.domain.service.save.strategy;

import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;

import com.kakaobean.core.notification.domain.event.SendDeploymentReleaseNoteNotificationEvent;
import com.kakaobean.core.notification.domain.event.SendNotificationEvent;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.exception.NotExistsReleaseNoteException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.notification.domain.NotificationType.*;

@RequiredArgsConstructor
public class SaveDeploymentReleaseNoteNotificationStrategy implements SaveNotificationStrategy {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final NotificationRepository notificationRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public SendNotificationEvent save(Long releaseNoteId) {
        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NotExistsReleaseNoteException::new);
        Project project = projectRepository.findById(releaseNote.getProjectId())
                .orElseThrow(NotExistsProjectException::new);
        List<String> emails = saveNotifications(releaseNote);
        return new SendDeploymentReleaseNoteNotificationEvent(
                releaseNote.getProjectId(),
                releaseNote.getTitle(),
                emails,
                releaseNoteId,
                project.getTitle()
        );
    }

    private List<String> saveNotifications(ReleaseNote releaseNote) {
        return projectQueryRepository
                .findProjectMembers(releaseNote.getProjectId())
                .stream()
                .map((dto -> {
                    notificationRepository.save(new Notification(ACTIVE, dto.getProjectMemberId(), releaseNote.getProjectId(), releaseNote.getTitle(), RELEASE_NOTE_DEPLOYMENT, false));
                    return dto.getProjectMemberEmail();
                }))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == RELEASE_NOTE_DEPLOYMENT;
    }
}
