package com.kakaobean.core.unit.application.sprint;

import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.sprint.Exception.IllegalSprintDateException;
import com.kakaobean.core.sprint.Exception.SprintAccessException;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.SprintValidator;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.sprint.dto.RegisterSprintRequestDtoFactory.createDto;
import static com.kakaobean.core.factory.sprint.dto.RegisterSprintRequestDtoFactory.createFailDto;
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

    @BeforeEach
    void beforeEach(){
        sprintService = new SprintService(
                sprintRepository,
                new SprintValidator(projectMemberRepository)
        );
    }

    @Test
    void 관리자가_스프린트를_등록한다(){
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createAdmin()));

        // when
        sprintService.registerSprint(createDto(1L, 2L));

        // then
        verify(sprintRepository, times(1)).save(Mockito.any(Sprint.class));
    }

    @Test
    void 일반멤버는_스프린트를_등록할_수_없다(){
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(1L, 2L));
        });

        // then
        result.isInstanceOf(SprintAccessException.class);
    }

    @Test
    void 시작날짜보다_빠른_마감날짜로는_스프린트를_생성하지_못한다(){
        // given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createAdmin()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            sprintService.registerSprint(createFailDto(1L, 2L));
        });

        // then
        result.isInstanceOf(IllegalSprintDateException.class);
    }
}
