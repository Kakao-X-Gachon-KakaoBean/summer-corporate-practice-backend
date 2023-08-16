package com.kakaobean.core.integration.sprint;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.sprint.TaskFactory;
import com.kakaobean.core.factory.sprint.dto.ModifyTaskRequestDtoFactory;
import com.kakaobean.core.factory.sprint.dto.RegisterTaskRequestDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.WorkStatus;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.core.sprint.exception.AssignmentNotAllowedException;
import com.kakaobean.core.sprint.exception.ChangeOperationNotAllowedException;
import com.kakaobean.core.sprint.exception.TaskAccessException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.factory.project.ProjectFactory.createWithoutId;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createWithMemberIdAndProjectId;
import static com.kakaobean.core.factory.sprint.SprintFactory.createWithId;
import static com.kakaobean.core.factory.sprint.dto.ChangeWorkStatusRequestDtoFactory.createWithId;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskServiceTest extends IntegrationTest {

    @Autowired
    TaskService taskService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        sprintRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        projectMemberRepository.deleteAll();
    }

    @Test
    void 테스크를_생성한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));

        // when
        taskService.registerTask(RegisterTaskRequestDtoFactory.createWithId(sprint.getId(), projectMember.getMemberId()));

        // then
        assertThat(taskRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 일반멤버는_테스크를_생성하지_못한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.registerTask(RegisterTaskRequestDtoFactory.createWithId(sprint.getId(), projectMember.getMemberId()));
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 테스크를_수정한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), 1L));

        // when
        taskService.modifyTask(ModifyTaskRequestDtoFactory.createWithId(task.getId(), sprint.getId(), projectMember.getMemberId()));

        // then
        assertThat(taskRepository.findAll().get(0).getTitle()).isEqualTo("새로운 테스크 제목");
    }

    @Test
    void 일반멤버는_테스크를_수정하지_못한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), 1L));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.modifyTask(ModifyTaskRequestDtoFactory.createWithId(task.getId(), sprint.getId(), projectMember.getMemberId()));
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 테스크를_삭제한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), 1L));

        // when
        taskService.removeTask(projectMember.getMemberId(), task.getId());

        // then
        assertThat(taskRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void 일반멤버는_테스크를_삭제하지_못한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), 1L));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.removeTask(projectMember.getMemberId(), task.getId());
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 어드민이_멤버에게_테스크를_할당한다() {
        // given
        Member member = memberRepository.save(MemberFactory.create());
        Member invitedMember = memberRepository.save(MemberFactory.createWithoutId());

        Project project = projectRepository.save(createWithoutId());
        ProjectMember projectAdmin = projectMemberRepository.save(createWithMemberIdAndProjectId(member.getId(), project.getId(), ADMIN));
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(invitedMember.getId(), project.getId(), MEMBER));

        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), null));

        // when
        taskService.assignTask(projectAdmin.getMemberId(), task.getId(), projectMember.getMemberId());

        // then
        assertThat(taskRepository.findById(task.getId()).get().getWorkerId()).isEqualTo(projectMember.getMemberId());
    }

    @Test
    void 일반멤버는_테스크를_할당할_수_없다() {
        // given
        Project project = projectRepository.save(createWithoutId());
        ProjectMember projectAdmin = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(2L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), null));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.assignTask(projectAdmin.getMemberId(), task.getId(), projectMember.getMemberId());
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void Viewr는_테스크를_할당받을_수_없다() {
        // given
        Project project = projectRepository.save(createWithoutId());
        ProjectMember projectAdmin = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        ProjectMember projectViewer = projectMemberRepository.save(createWithMemberIdAndProjectId(2L, project.getId(), VIEWER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), null));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.assignTask(projectAdmin.getMemberId(), task.getId(), projectViewer.getMemberId());
        });

        // then
        result.isInstanceOf(AssignmentNotAllowedException.class);
    }

    @Test
    void 테스크_담당가_작업상태를_변경한다() {
        // given
        Project project = projectRepository.save(createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), projectMember.getMemberId()));

        // when
        taskService.changeStatus(createWithId(task.getWorkerId(), task.getId()));

        // then
        assertThat(taskRepository.findById(task.getId()).get().getWorkStatus()).isEqualTo(WorkStatus.COMPLETE);
    }

    @Test
    void 테스크_담당자가_아니라면_작업상태를_변경할_수_없다() {
        // given
        Project project = projectRepository.save(createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        ProjectMember projectMember2 = projectMemberRepository.save(createWithMemberIdAndProjectId(2L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));
        Task task = taskRepository.save(TaskFactory.createWithId(sprint.getId(), projectMember.getMemberId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.changeStatus(createWithId(projectMember2.getMemberId(), task.getId()));
        });

        // then
        result.isInstanceOf(ChangeOperationNotAllowedException.class);
    }
}
