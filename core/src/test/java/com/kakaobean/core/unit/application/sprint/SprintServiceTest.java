package com.kakaobean.core.unit.application.sprint;

import com.kakaobean.core.factory.sprint.SprintFactory;
import com.kakaobean.core.factory.sprint.dto.ModifySprintRequestDtoFactory;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.sprint.exception.InvalidSprintDateException;
import com.kakaobean.core.sprint.exception.SprintAccessException;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.SprintValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static com.kakaobean.core.factory.sprint.dto.RegisterSprintRequestDtoFactory.createDto;
import static com.kakaobean.core.factory.sprint.dto.RegisterSprintRequestDtoFactory.createFailDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SprintServiceTest extends UnitTest {

    SprintService sprintService;

    @Mock
    SprintRepository sprintRepository;

    @Mock
    ProjectMemberRepository projectMemberRepository;

    @Mock
    TaskRepository taskRepository;

    @BeforeEach
    void beforeEach() {
        sprintService = new SprintService(
                sprintRepository,
                taskRepository,
                new SprintValidator(projectMemberRepository)
        );
    }

    @Test
    void 관리자가_스프린트를_등록한다() {
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()));

        // when
        sprintService.registerSprint(createDto(1L, 2L));

        // then
        verify(sprintRepository, times(1)).save(Mockito.any(Sprint.class));
    }

    @Test
    void 일반멤버는_스프린트를_등록할_수_없다() {
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(1L, 2L));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로는_스프린트를_생성하지_못한다() {
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(1L, 2L));
        });

        // then
        result.isInstanceOf(InvalidSprintDateException.class);
    }

    @Test
    void 관리자가_스프린트를_수정한다() {
        // given
        Sprint testSprint = SprintFactory.createWithId(1L);
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(testSprint));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()));

        // when
        sprintService.modifySprint(ModifySprintRequestDtoFactory.createWithId(2L, 1L));

        // then
        assertThat(testSprint.getTitle()).isEqualTo("수정된 스프린트 제목");
    }

    @Test
    void 일반멤버는_스프린트를_수정할_수_없다() {
        // given
        Sprint testSprint = SprintFactory.createWithId(1L);
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(testSprint));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.modifySprint(ModifySprintRequestDtoFactory.createWithId(2L, 1L));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로_수정할_수_없다() {
        // given
        Sprint testSprint = SprintFactory.createWithId(1L);
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(testSprint));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.modifySprint(ModifySprintRequestDtoFactory.createFailWithId(2L, 1L));
        });

        // then
        result.isInstanceOf(InvalidSprintDateException.class);
    }

    @Test
    void 관리자가_스프린트를_삭제한다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(SprintFactory.createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()));

        // when
        sprintService.removeSprint(1L, 2L);

        // then
        verify(sprintRepository, times(1)).delete(Mockito.any(Sprint.class));
        verify(taskRepository, times(1)).deleteBySprintId(Mockito.anyLong());
    }

    @Test
    void 일반멤버는_스프린트를_삭제할_수_없다() {
        // given
        given(sprintRepository.findById(Mockito.anyLong())).willReturn(Optional.of(SprintFactory.createWithId(1L)));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.removeSprint(1L, 2L);
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }
}
