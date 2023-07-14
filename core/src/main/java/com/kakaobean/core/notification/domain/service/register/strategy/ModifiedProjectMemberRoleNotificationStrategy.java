package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.SendRegisterManuscriptNotificationEvent;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class ModifiedProjectMemberRoleNotificationStrategy implements RegisterNotificationStrategy {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationSentEvent register(Long projectMemberId) {
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(NotExistsProjectMemberException::new);
        String url = "/projects/" + projectMember.getProjectId();
        notificationRepository.save(new Notification(ACTIVE, projectMemberId, url, false));
        Project project = projectRepository.findById(projectMember.getProjectId())
                .orElseThrow(NotExistsProjectException::new);
        return new SendRegisterManuscriptNotificationEvent(url, project.getTitle(), "", LocalDateTime.now(), project.getId());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == MODIFIED_PROJECT_MEMBER_ROLE;
    }
}
