package com.kakaobean.core.integration.sprint;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.sprint.SprintFactory;
import com.kakaobean.core.factory.sprint.TaskFactory;
import com.kakaobean.core.factory.sprint.dto.ModifySprintRequestDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.exception.InvalidSprintDateException;
import com.kakaobean.core.sprint.exception.SprintAccessException;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
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
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Test
    void 관리자가_스프린트를_등록한다() {


        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));

        // when
        Long springId = sprintService.registerSprint(createDto(projectMember.getMemberId(), project.getId()));

        // then
        assertThat(sprintRepository.findById(springId).isPresent()).isTrue();
    }

    @Test
    void 일반멤버는_스프린트를_등록할_수_없다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(projectMember.getMemberId(), project.getId()));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로는_스프린트를_생성하지_못한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(projectMember.getMemberId(), project.getId()));
        });

        // then
        result.isInstanceOf(InvalidSprintDateException.class);
    }

    @Test
    void 관리자가_스프린트를_수정한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        // when
        sprintService.modifySprint(ModifySprintRequestDtoFactory.createWithId(projectMember.getMemberId(), sprint.getId()));

        // then
        assertThat(sprintRepository.findById(sprint.getId()).get().getTitle()).isEqualTo("수정된 스프린트 제목");
    }

    @Test
    void 일반멤버는_스프린트를_수정할_수_없다() {
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.modifySprint(ModifySprintRequestDtoFactory.createWithId(projectMember.getMemberId(), sprint.getId()));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로_수정할_수_없다() {
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.modifySprint(ModifySprintRequestDtoFactory.createFailWithId(projectMember.getMemberId(), sprint.getId()));
        });

        // then
        result.isInstanceOf(InvalidSprintDateException.class);
    }

    @Test
    void 관리자가_스프린트를_삭제한다() {

        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));
        Task task1 = taskRepository.save(TaskFactory.createWithId(sprint.getId(), MemberFactory.getMemberId()));
        Task task2 = taskRepository.save(TaskFactory.createWithId(sprint.getId(), MemberFactory.getMemberId()));

        // when
        sprintService.removeSprint(projectMember.getMemberId(), sprint.getId());

        // then
        assertThat(sprintRepository.findById(sprint.getId()).isEmpty()).isTrue();
        assertThat(taskRepository.findById(task1.getId()).isEmpty()).isTrue();
        assertThat(taskRepository.findById(task2.getId()).isEmpty()).isTrue();

    }

    @Test
    void 일반멤버는_스프린트를_삭제할_수_없다() {

        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));

        taskRepository.save(TaskFactory.createWithId(sprint.getId(), MemberFactory.getMemberId()));
        taskRepository.save(TaskFactory.createWithId(sprint.getId(), MemberFactory.getMemberId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.removeSprint(projectMember.getMemberId(), sprint.getId());
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

}
