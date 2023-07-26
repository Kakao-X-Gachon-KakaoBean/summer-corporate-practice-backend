package com.kakaobean.core.unit.application.sprint;

import com.kakaobean.core.factory.sprint.TaskFactory;
import com.kakaobean.core.factory.sprint.dto.ModifyTaskRequestDtoFactory;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.sprint.Exception.NotExistsTaskException;
import com.kakaobean.core.sprint.Exception.TaskAccessException;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.TaskValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.createAdmin;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createMember;
import static com.kakaobean.core.factory.sprint.SprintFactory.createWithId;
import static com.kakaobean.core.factory.sprint.dto.RegisterTaskRequestDtoFactory.createWithId;
import static org.assertj.core.api.Assertions.*;
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
    
    @BeforeEach
    void beforeEach(){
        taskService = new TaskService(
                taskRepository,
                sprintRepository,
                new TaskValidator(projectMemberRepository)
        );
    }

    @Test
    void 테스크를_생성한다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));

        // when
        taskService.registerTask(createWithId(1L,2L));

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
        taskService.modifyTask(ModifyTaskRequestDtoFactory.createWithId(1L,1L,2L));

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
            taskService.modifyTask(ModifyTaskRequestDtoFactory.createWithId(1L,1L,2L));
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

    @Test
    void 테스크를_삭제한다() {
        // given
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(TaskFactory.createWithId(1L,2L)));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));

        // when
        taskService.removeTask(1L,2L);

        // then
        verify(taskRepository,times(1)).delete(Mockito.any(Task.class));
    }

    @Test
    void 일반멤버는_테스크를_삭제하지_못한다() {
        // given
        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(TaskFactory.createWithId(1L,2L)));
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.removeTask(1L,2L);
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }
    
}
