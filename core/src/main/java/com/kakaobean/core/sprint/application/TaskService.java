package com.kakaobean.core.sprint.application;

import com.kakaobean.core.sprint.Exception.NotExistsSprintException;
import com.kakaobean.core.sprint.Exception.NotExistsTaskException;
import com.kakaobean.core.sprint.application.dto.ModifyTaskRequestDto;
import com.kakaobean.core.sprint.application.dto.RegisterTaskRequestDto;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.TaskValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final TaskValidator taskValidator;

    @Transactional
    public void registerTask(RegisterTaskRequestDto dto) {
        Sprint sprint = sprintRepository.findById(dto.getSprintId()).orElseThrow(NotExistsSprintException::new);
        taskValidator.validate(dto.getAdminId(), sprint.getProjectId());
        Task task = dto.toEntity();
        taskRepository.save(task);
    }

    @Transactional
    public void modifyTask(ModifyTaskRequestDto dto) {
        Sprint sprint = sprintRepository.findById(dto.getSprintId()).orElseThrow(NotExistsSprintException::new);
        taskValidator.validate(dto.getAdminId(), sprint.getProjectId());
        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow(NotExistsTaskException::new);
        task.modify(dto.getNewTitle(), dto.getNewContent());
    }
}
