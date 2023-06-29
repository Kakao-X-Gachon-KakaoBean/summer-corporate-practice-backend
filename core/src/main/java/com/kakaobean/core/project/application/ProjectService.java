package com.kakaobean.core.project.application;

import com.kakaobean.core.project.application.dto.Response.RegisterProjectResponseDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = false)
    public RegisterProjectResponseDto registerProject(RegisterProjectRequestDto dto, Long memberId) {
        Project project = dto.toEntity();
        Project saveProject = projectRepository.save(project);
        return new RegisterProjectResponseDto(saveProject.getId());
    }

}
