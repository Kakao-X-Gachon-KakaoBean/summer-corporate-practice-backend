package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.ModifiedProjectMemberNotificationEvent;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.RegisterManuscriptNotificationEvent;
import com.kakaobean.core.notification.utils.NotificationUtils;
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

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationSentEvent register(Long projectMemberId) {

        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(NotExistsProjectMemberException::new);
        Project project = projectRepository.findById(projectMember.getProjectId())
                .orElseThrow(NotExistsProjectException::new);
        Member member = memberRepository.findById(projectMember.getMemberId())
                .orElseThrow(NotExistsMemberException::new);

        String url = "/projects/" + projectMember.getProjectId();
        String content = project.getTitle() + " 프로젝트의 권한이 " + projectMember.getProjectRole().name() + " 으로 변경되었습니다.";
        String finalContent = NotificationUtils.makeNotificationContent(project.getTitle(), content);
        notificationRepository.save(new Notification(ACTIVE, projectMemberId, url, false, finalContent));
        return new ModifiedProjectMemberNotificationEvent(url, project.getTitle(), content, LocalDateTime.now(), member.getAuth().getEmail(), member.getId());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == MODIFIED_PROJECT_MEMBER_ROLE;
    }
}
