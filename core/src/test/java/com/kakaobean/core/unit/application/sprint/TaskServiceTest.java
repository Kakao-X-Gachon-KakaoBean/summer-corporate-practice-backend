package com.kakaobean.core.unit.application.sprint;

import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.sprint.Exception.TaskAccessException;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.TaskValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.createAdmin;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createMember;
import static com.kakaobean.core.factory.sprint.SprintFactory.createWithId;
import static com.kakaobean.core.factory.sprint.dto.RegisterTaskRequestDtoFactory.createWithId;
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
    
}
