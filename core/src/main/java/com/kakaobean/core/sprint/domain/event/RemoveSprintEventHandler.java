package com.kakaobean.core.sprint.domain.event;

import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveSprintEventHandler {

    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;

    @Async
    @Transactional
    @TransactionalEventListener(RemovedProjectEvent.class)
    public void handler(RemovedProjectEvent event){
        List<Sprint> sprints = sprintRepository.findSprintByProjectId(event.getProjectId());
        for (Sprint sprint: sprints) {
            taskRepository.deleteBySprintId(sprint.getId());
        }
        sprintRepository.deleteByProjectId(event.getProjectId());
    }

}
