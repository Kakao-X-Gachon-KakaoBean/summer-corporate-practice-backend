package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.domain.event.RegisterCommentNotificationEvent;
import com.kakaobean.core.notification.utils.NotificationUtils;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.issue.exception.NotExistsCommentException;
import com.kakaobean.core.issue.exception.NotExistsIssueException;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.notification.domain.NotificationType.POSTED_COMMENT;

@Component
@RequiredArgsConstructor
public class RegisterCommentNotificationStrategy implements RegisterNotificationStrategy {

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final IssueRepository issueRepository;

    private final ProjectRepository projectRepository;

    private final NotificationRepository notificationRepository;


    @Override
    public NotificationSentEvent register(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(NotExistsCommentException::new);
        Member worker = memberRepository.findById(comment.getWriterId()).orElseThrow(NotExistsMemberException::new);
        Issue issue = issueRepository.findById(comment.getIssueId()).orElseThrow(NotExistsIssueException::new);
        Project project = projectRepository.findById(issue.getProjectId()).orElseThrow(NotExistsProjectException::new);

        String url = "/projects/" + project.getId() + "/issues/" + issue.getId();
        String content = issue.getTitle() + "새로운 댓글이 달렸습니다.";
        String finalContent = NotificationUtils.makeNotificationContent(project.getTitle(), content);
        notificationRepository.save(new Notification(ACTIVE, issue.getWriterId(), url, false, finalContent));
        return new RegisterCommentNotificationEvent(url, project.getTitle(), finalContent, LocalDateTime.now(), worker.getAuth().getEmail(), issue.getWriterId(), issue.getId(), project.getId());
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == POSTED_COMMENT;
    }

}
