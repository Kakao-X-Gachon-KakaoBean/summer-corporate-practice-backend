package com.kakaobean.core.sprint.application;

import com.kakaobean.core.sprint.exception.NotExistsTaskException;
import com.kakaobean.core.sprint.application.dto.ChangeWorkStatusRequestDto;
import com.kakaobean.core.sprint.application.dto.ModifyTaskRequestDto;
import com.kakaobean.core.sprint.application.dto.RegisterTaskRequestDto;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.TaskValidator;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;

    @Transactional
    public void registerTask(RegisterTaskRequestDto dto) {
        taskValidator.validate(dto.getAdminId(), dto.getSprintId());
        Task task = dto.toEntity();
        taskRepository.save(task);
    }

    @Transactional
    public void modifyTask(ModifyTaskRequestDto dto) {
        taskValidator.validate(dto.getAdminId(), dto.getSprintId());
        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow(NotExistsTaskException::new);
        task.modify(dto.getNewTitle(), dto.getNewContent());
    }

    @Transactional
    public void removeTask(Long adminId, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(NotExistsTaskException::new);
        taskValidator.validate(adminId, task.getSprintId());
        taskRepository.delete(task);
    }

    @Transactional
    public void assignTask(Long adminId, Long taskId, Long memberId) {
        Task task = taskRepository.findById(taskId).orElseThrow(NotExistsTaskException::new);
        taskValidator.validAssignmentTask(task, adminId,  memberId);
        task.assigned(memberId);
    }

    @Transactional
    public void changeStatus(ChangeWorkStatusRequestDto dto) {
        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow(NotExistsTaskException::new);
        taskValidator.validRightToChange(task, dto.getWorkerId());
        task.changeStatus(dto.getWorkStatus());
    }
}
