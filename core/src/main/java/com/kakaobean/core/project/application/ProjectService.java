package com.kakaobean.core.project.application;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional(readOnly = false)
    public RegisterProjectResponseDto registerProject(RegisterProjectRequestDto dto) {
        Project project = dto.toEntity();
        Project saveProject = projectRepository.save(project);
        projectMemberRepository.save(new ProjectMember(BaseStatus.ACTIVE, saveProject.getId(), dto.getAdminId(), ProjectRole.ADMIN));
        return new RegisterProjectResponseDto(saveProject.getId());
    }
}
