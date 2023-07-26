package com.kakaobean.core.sprint.application;

import com.kakaobean.core.sprint.Exception.NotExistsSprintException;
import com.kakaobean.core.sprint.application.dto.ModifySprintRequestDto;
import com.kakaobean.core.sprint.application.dto.RegisterSprintRequestDto;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.SprintValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final SprintValidator sprintValidator;

    @Transactional
    public void registerSprint(RegisterSprintRequestDto dto) {
        Sprint sprint = dto.toEntity();
        sprintValidator.validate(sprint, dto.getMemberId());
        sprintRepository.save(sprint);
    }

    @Transactional
    public void modifySprint(ModifySprintRequestDto dto) {
        Sprint sprint = sprintRepository.findById(dto.getSprintId()).orElseThrow(NotExistsSprintException::new);
        sprint.modify(dto.getNewTitle(),dto.getNewDescription(),dto.getNewStartDate(),dto.getNewEndDate());
        sprintValidator.validate(sprint, dto.getAdminId());
    }

    @Transactional
    public void removeSprint(Long memberId, Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(NotExistsSprintException::new);
        sprintValidator.validateRemoveAccess(sprint, memberId);
        sprintRepository.delete(sprint);
        taskRepository.deleteBySprintId(sprintId);
    }
}
