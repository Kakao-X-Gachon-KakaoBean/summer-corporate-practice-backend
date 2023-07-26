package com.kakaobean.core.sprint.application;

import com.kakaobean.core.sprint.application.dto.RegisterTaskRequestDto;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.TaskValidator;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;

    public void registerTask(RegisterTaskRequestDto dto) {
        taskValidator.validate(dto.getAdminId(), dto.getProjectId());
        Task task = dto.toEntity();
        taskRepository.save(task);
    }

}
