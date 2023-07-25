package com.kakaobean.core.integration.sprint;

import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.sprint.SprintFactory;
import com.kakaobean.core.factory.sprint.dto.ModifySprintRequestDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.Exception.IllegalSprintDateException;
import com.kakaobean.core.sprint.Exception.SprintAccessException;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.createWithMemberIdAndProjectId;
import static com.kakaobean.core.factory.sprint.dto.RegisterSprintRequestDtoFactory.createDto;
import static com.kakaobean.core.factory.sprint.dto.RegisterSprintRequestDtoFactory.createFailDto;
import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;
import static com.kakaobean.core.project.domain.ProjectRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SprintServiceTest extends IntegrationTest {

    @Autowired
    SprintService sprintService;

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach(){
        sprintRepository.deleteAll();
        projectRepository.deleteAll();
        projectMemberRepository.deleteAll();
    }

    @Test
    void 관리자가_스프린트를_등록한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));

        // when
        sprintService.registerSprint(createDto(projectMember.getMemberId(), project.getId()));

        // then
        assertThat(sprintRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 일반멤버는_스프린트를_등록할_수_없다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(projectMember.getMemberId(), project.getId()));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로는_스프린트를_생성하지_못한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(projectMember.getMemberId(), project.getId()));
        });

        // then
        result.isInstanceOf(IllegalSprintDateException.class);
    }

    @Test
    void 관리자가_스프린트를_수정한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        // when
        sprintService.modifySprint(ModifySprintRequestDtoFactory.createDto(projectMember.getMemberId(), sprint.getId()));

        // then
        assertThat(sprintRepository.findById(sprint.getId()).get().getTitle()).isEqualTo("수정된 스프린트 제목");
    }

    @Test
    void 일반멤버는_스프린트를_수정할_수_없다(){
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.modifySprint(ModifySprintRequestDtoFactory.createDto(projectMember.getMemberId(), sprint.getId()));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로_수정할_수_없다(){
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.modifySprint(ModifySprintRequestDtoFactory.createFailDto(projectMember.getMemberId(), sprint.getId()));
        });

        // then
        result.isInstanceOf(IllegalSprintDateException.class);
    }


}
