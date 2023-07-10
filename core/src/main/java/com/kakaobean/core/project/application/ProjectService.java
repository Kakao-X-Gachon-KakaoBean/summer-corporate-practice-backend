package com.kakaobean.core.project.application;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoReqeustDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectValidator projectValidator;

    @Transactional(readOnly = false)
    public RegisterProjectResponseDto registerProject(RegisterProjectRequestDto dto) {
        Project project = dto.toEntity();
        Project savedProject = projectRepository.save(project);
        projectMemberRepository.save(new ProjectMember(BaseStatus.ACTIVE, savedProject.getId(), dto.getAdminId(), ProjectRole.ADMIN));
        return new RegisterProjectResponseDto(savedProject.getId());
    }

    @Transactional(readOnly = false)
    public void modifyProject(ModifyProjectInfoReqeustDto dto){
        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(dto.getAdminId(), dto.getProjectId()).orElseThrow(NotExistsProjectMemberException::new);
        projectValidator.validAdmin(projectAdmin);
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(NotExistsProjectException::new);
        project.modify(dto.getNewTitle(),dto.getNewContent());
    }

    @Transactional(readOnly = false)
    public void removeProject(Long adminId, Long projectId) {
        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(adminId, projectId).orElseThrow(NotExistsProjectMemberException::new);
        projectValidator.validAdmin(projectAdmin);
        Project project = projectRepository.findById(projectId).orElseThrow(NotExistsProjectException::new);
        project.remove();
    }
}
