package com.kakaobean.core.notification.domain.service.register.strategy;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.NotificationType;
import com.kakaobean.core.notification.domain.event.NotificationSentEvent;
import com.kakaobean.core.notification.utils.NotificationUtils;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.sprint.Exception.NotExistsSprintException;
import com.kakaobean.core.sprint.Exception.NotExistsTaskException;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.notification.domain.NotificationType.*;

@Component
@RequiredArgsConstructor
public class RegisterTaskAssignmentNotificationStrategy implements RegisterNotificationStrategy {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationSentEvent register(Long taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(NotExistsTaskException::new);
        Sprint sprint = sprintRepository.findById(task.getSprintId()).orElseThrow(NotExistsSprintException::new);
        Project project = projectRepository.findById(sprint.getProjectId()).orElseThrow(NotExistsProjectException::new);

        //  /projects/projectid/sprints/sprintId/taskId
        String url = "/projects/" + sprint.getProjectId() + "/sprints/" + sprint.getId() + "/" + task.getId();
        String content = task.getTitle() + " 작업이 할당되었습니다.";
        String finalContent = NotificationUtils.makeNotificationContent(project.getTitle(), content);
        notificationRepository.save(new Notification(ACTIVE, task.getWorkerId(), url, false, finalContent));
        return ;
    }

    @Override
    public boolean support(NotificationType notificationType) {
        return notificationType == TASK;
    }
}
