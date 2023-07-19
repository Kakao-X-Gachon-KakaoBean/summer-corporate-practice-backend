package com.kakaobean.core.unit.application.releasenote;

import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.factory.releasenote.ManuscriptFactory;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.ManuscriptValidator;
import com.kakaobean.core.releasenote.exception.DuplicateManuscriptVersionException;
import com.kakaobean.core.releasenote.exception.ManuscriptWriterAccessException;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class ManuscriptServiceTest extends UnitTest {

    ManuscriptService manuscriptService;

    @Mock
    ManuscriptRepository manuscriptRepository;

    @Mock
    ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach() {
        manuscriptService = new ManuscriptService(
                manuscriptRepository,
                new ManuscriptValidator(
                        projectMemberRepository,
                        manuscriptRepository
                )
        );
    }

    @Test
    void 릴리즈_노트_첫_원고를_등록한다() {
        RegisterManuscriptRequestDto dto = new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.1", 1L, 2L);
        given(manuscriptRepository.findManuscriptByVersion(anyString()))
                .willReturn(Optional.ofNullable(null));
        given(projectMemberRepository.findByMemberIdAndProjectId(anyLong(), anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createAdmin()));

        manuscriptService.registerManuscript(dto);

        verify(manuscriptRepository, times(1)).save(any(Manuscript.class));
    }

    @Test
    void 관리자만_릴리즈_노트_첫_원고를_등록할_수_있다() {
        RegisterManuscriptRequestDto dto = new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.1", 1L, 2L);
        given(projectMemberRepository.findByMemberIdAndProjectId(anyLong(), anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createMember()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.registerManuscript(dto);
        });

        result.isInstanceOf(ManuscriptWriterAccessException.class);
    }

    @Test
    void 중복되는_릴리즈_노트_원고_버전이_존재한다() {
        RegisterManuscriptRequestDto dto = new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.1", 1L, 2L);
        given(projectMemberRepository.findByMemberIdAndProjectId(anyLong(), anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createAdmin()));
        given(manuscriptRepository.findManuscriptByVersion(anyString()))
                .willReturn(Optional.ofNullable(ManuscriptFactory.create()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.registerManuscript(dto);
        });

        result.isInstanceOf(DuplicateManuscriptVersionException.class);
    }
}
