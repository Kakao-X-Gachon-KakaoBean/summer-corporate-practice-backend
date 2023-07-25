package com.kakaobean.core.sprint.application;

import com.kakaobean.core.sprint.application.dto.RegisterSprintRequestDto;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.SprintValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final SprintRepository sprintRepository;
    private final SprintValidator sprintValidator;

    @Transactional
    public void registerSprint(RegisterSprintRequestDto dto) {
        Sprint sprint = dto.toEntity();
        sprintValidator.validate(sprint, dto.getMemberId());
        sprintRepository.save(sprint);
    }
}
