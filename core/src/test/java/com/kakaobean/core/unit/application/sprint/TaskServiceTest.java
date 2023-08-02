package com.kakaobean.core.unit.application.sprint;

import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.factory.sprint.SprintFactory;
import com.kakaobean.core.factory.sprint.TaskFactory;
import com.kakaobean.core.factory.sprint.dto.ChangeWorkStatusRequestDtoFactory;
import com.kakaobean.core.factory.sprint.dto.ModifyTaskRequestDtoFactory;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.sprint.Exception.AssignmentNotAllowedException;
import com.kakaobean.core.sprint.Exception.ChangeOperationNotAllowedException;
import com.kakaobean.core.sprint.Exception.TaskAccessException;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.TaskValidator;
import com.kakaobean.core.sprint.domain.WorkStatus;
import com.kakaobean.core.sprint.domain.event.TaskAssignedEvent;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectFactory.createWithoutId;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static com.kakaobean.core.factory.sprint.SprintFactory.createWithId;
import static com.kakaobean.core.factory.sprint.dto.RegisterTaskRequestDtoFactory.createWithId;
import static com.kakaobean.core.project.domain.ProjectRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

public class TaskServiceTest extends UnitTest {

    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    SprintRepository sprintRepository;

    @Mock
    ProjectMemberRepository projectMemberRepository;

    private static MockedStatic<Events> mockEvents;

    @BeforeEach
    void beforeEach() {
        taskService = new TaskService(
                taskRepository,
                new TaskValidator(projectMemberRepository, sprintRepository)
        );
        mockEvents = mockStatic(Events.class);
    }

    @AfterEach
    void afterEach(){
        mockEvents.close();
    }

    @Test
    void 테스크를_생성한다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));

        // when
        taskService.registerTask(createWithId(1L, 2L));

        // then
        verify(taskRepository, times(1)).save(Mockito.any(Task.class));
    }

    @Test
    void 일반멤버는_테스크를_생성하지_못한다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.registerTask(createWithId(1L, 2L));
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 테스크를_수정한다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));

        Task task = TaskFactory.createWithId(1L, 2L);
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));

        // when
        taskService.modifyTask(ModifyTaskRequestDtoFactory.createWithId(1L, 1L, 2L));

        // then
        assertThat(task.getTitle()).isEqualTo("새로운 테스크 제목");
    }

    @Test
    void 일반멤버는_테스크를_수정하지_못한다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.modifyTask(ModifyTaskRequestDtoFactory.createWithId(1L, 1L, 2L));
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 테스크를_삭제한다() {
        // given
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(TaskFactory.createWithId(1L, 2L)));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));

        // when
        taskService.removeTask(1L, 2L);

        // then
        verify(taskRepository, times(1)).delete(Mockito.any(Task.class));
    }

    @Test
    void 일반멤버는_테스크를_삭제하지_못한다() {
        // given
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(TaskFactory.createWithId(1L, 2L)));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.removeTask(1L, 2L);
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 어드민이_멤버에게_테스크를_할당한다() {
        // given
        Task task = TaskFactory.createWithId(1L, null);
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()), Optional.of(createMember()));

        // when
        taskService.assignTask(1L, 2L, 3L);

        // then
        assertThat(task.getWorkerId()).isNotNull();
        assertThat(task.getWorkStatus()).isEqualTo(WorkStatus.WORKING);
        mockEvents.verify(() -> Events.raise(Mockito.any(TaskAssignedEvent.class)), times(1));
    }

    @Test
    void 일반멤버는_테스크를_할당할_수_없다() {
        // given
        Task task = TaskFactory.createWithId(1L, null);
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.assignTask(1L, 2L, 3L);
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void Viewr는_테스크를_할당받을_수_없다() {
        // given
        Task task = TaskFactory.createWithId(1L, null);
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()), Optional.of(createViewer()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.assignTask(1L, 2L, 3L);
        });

        // then
        result.isInstanceOf(AssignmentNotAllowedException.class);
    }

    @Test
    void 테스크_담당가_작업상태를_변경한다() {
        // given
        Long workerId = 2L;
        Task task = TaskFactory.createWithId(1L, workerId);

        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(SprintFactory.createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(),Mockito.anyLong())).willReturn(Optional.of(createMember()));

        // when
        taskService.changeStatus(ChangeWorkStatusRequestDtoFactory.createWithId(workerId, 2L));

        // then
        assertThat(task.getWorkStatus()).isEqualTo(WorkStatus.COMPLETE);
    }

    @Test
    void 테스크_담당자가_아니라면_작업상태를_변경할_수_없다() {
        // given
        Long workerId = 2L;
        Long differentWorkerId = 3L;
        Task task = TaskFactory.createWithId(1L, workerId);

        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(SprintFactory.createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(),Mockito.anyLong())).willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.changeStatus(ChangeWorkStatusRequestDtoFactory.createWithId(differentWorkerId, 2L));
        });

        // then
        result.isInstanceOf(ChangeOperationNotAllowedException.class);
    }

}
