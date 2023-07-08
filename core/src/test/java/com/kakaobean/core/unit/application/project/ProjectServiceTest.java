package com.kakaobean.core.unit.application.project;

import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoReqeustDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectFactory.create;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createAdmin;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void 어드민이_프로젝트_정보를_수정에_성공한다() {
        // given
        Project testProject = create();
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));
        given(projectRepository.findProjectById(Mockito.anyLong())).willReturn(Optional.of(testProject));
        // when
        projectService.modifyProject(new ModifyProjectInfoReqeustDto(1L, 2L, "새로운 프로젝트 제목", "새로운 프로젝트 내용"));
        // then
        assertThat(testProject.getTitle()).isEqualTo("새로운 프로젝트 제목");
    }

    @Test
    void 일반유저는_프로젝트_정보를_수정에_실패한다() {
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));
        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectService.modifyProject(new ModifyProjectInfoReqeustDto(1L, 2L, "새로운 프로젝트 제목", "새로운 프로젝트 내용"));
        });
        // then
        result.isInstanceOf(NotProjectAdminException.class);
    }
}
