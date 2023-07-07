package com.kakaobean.core.unit.application.project;

import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoReqeustDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.unit.UnitTest;
import com.mysema.commons.lang.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectFactory.create;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProjectServiceTest extends UnitTest {

    private ProjectService projectService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    void beforeEach() {
        projectService = new ProjectService(
                projectRepository,
                projectMemberRepository,
                new ProjectValidator()
        );
    }

    @Test
    void 로그인한_유저가_프로젝트_생성에_성공한다() {
        //given
        given(projectRepository.save(Mockito.any(Project.class))).willReturn(create());

        //when
        RegisterProjectResponseDto responseDto = projectService.registerProject(new RegisterProjectRequestDto("프로젝트 제목", "프로젝트 설명", 1L));

        //then
        verify(projectRepository, times(1)).save(Mockito.any(Project.class));
    }
}
