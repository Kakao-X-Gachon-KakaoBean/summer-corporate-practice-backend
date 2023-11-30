package com.kakaobean.core.unit.application.releasenote;

import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.factory.releasenote.ManuscriptFactory;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.request.ModifyManuscriptRequestDto;
import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptStatus;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.ManuscriptValidator;
import com.kakaobean.core.releasenote.exception.*;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.kakaobean.core.releasenote.domain.ManuscriptStatus.*;
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
        RegisterManuscriptRequestDto dto = new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.2", 1L, 2L);
        given(manuscriptRepository.findManuscriptByProjectId(anyLong()))
                .willReturn(List.of(ManuscriptFactory.create()));
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

        Manuscript manuscript = ManuscriptFactory.create();
        RegisterManuscriptRequestDto dto = new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", manuscript.getVersion(), 1L, 2L);
        given(projectMemberRepository.findByMemberIdAndProjectId(anyLong(), anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createAdmin()));
        given(manuscriptRepository.findManuscriptByProjectId(anyLong()))
                .willReturn(List.of(manuscript));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.registerManuscript(dto);
        });

        result.isInstanceOf(DuplicateManuscriptVersionException.class);
    }

    @Test
    void 릴리즈_노트_원고의_수정_권한을_얻는다() {

        Manuscript manuscript = ManuscriptFactory.create();
        given(manuscriptRepository.findByIdWithPESSIMISTICLock(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createAdmin()));

        manuscriptService.hasRightToModifyManuscript(1L, 2L);

        assertThat(manuscript.getManuscriptStatus()).isSameAs(Modifying);
    }

    @Test
    void 뷰어는_릴리즈_노트_원고를_수정_권한을_얻을_수_없다() {

        Manuscript manuscript = ManuscriptFactory.create();
        given(manuscriptRepository.findByIdWithPESSIMISTICLock(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createViewer()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.hasRightToModifyManuscript(1L, 2L);
        });

        result.isInstanceOf(ManuscriptModificationAccessException.class);
    }

    @Test
    void 다른_멤버가_릴리즈_노트_원고를_수정_중이면_원고를_수정할_권한을_얻을_수_없다() {
        Manuscript manuscript = ManuscriptFactory.create();
        given(manuscriptRepository.findByIdWithPESSIMISTICLock(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createAdmin()));
        manuscriptService.hasRightToModifyManuscript(1L, 2L);


        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.hasRightToModifyManuscript(2L, 2L);
        });

        result.isInstanceOf(AnotherMemberAlreadyModifyingException.class);
    }

    @Test
    void 릴리즈_노트_원고를_수정한다() {

        Manuscript manuscript = ManuscriptFactory.create(Modifying);
        ModifyManuscriptRequestDto dto = ManuscriptFactory.createServiceDto(1L, 2L);
        given(manuscriptRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createAdmin()));

        manuscriptService.modifyManuscript(dto);

        assertThat(manuscript.getManuscriptStatus()).isSameAs(Modifiable);
        assertThat(manuscript.getTitle()).isEqualTo(dto.getTitle());
        assertThat(manuscript.getContent()).isEqualTo(dto.getContent());
        assertThat(manuscript.getVersion()).isEqualTo(dto.getVersion());
        assertThat(manuscript.getLastEditedMemberId()).isSameAs(dto.getEditingMemberId());
    }

    @Test
    void 뷰어는_릴리즈_노트_원고를_수정할_수_없다() {

        Manuscript manuscript = ManuscriptFactory.create(Modifying);
        ModifyManuscriptRequestDto dto = ManuscriptFactory.createServiceDto(1L, 2L);
        given(manuscriptRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createViewer()));


        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.modifyManuscript(dto);
        });

        result.isInstanceOf(ManuscriptModificationAccessException.class);
    }

    @Test
    void 릴리즈_노트_원고_상태가_수정중이_아니면_수정할_수_없다() {
        Manuscript manuscript = ManuscriptFactory.create(Modifiable);
        ModifyManuscriptRequestDto dto = ManuscriptFactory.createServiceDto(1L, 2L);

        given(manuscriptRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createAdmin()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.modifyManuscript(dto);
        });

        result.isInstanceOf(CannotModifyManuscriptException.class);
    }

    @Test
    void 릴리즈_노트_원고를_삭제한다() {
        Manuscript manuscript = ManuscriptFactory.create();
        given(manuscriptRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createAdmin()));

        manuscriptService.deleteManuscript(1L, 2L);

        verify(manuscriptRepository, times(1)).delete(Mockito.any(Manuscript.class));
    }

    @Test
    void 릴리즈_노트는_관리자가_아니라면_삭제할_수_없다() {
        Manuscript manuscript = ManuscriptFactory.create();
        given(manuscriptRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(manuscript));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.ofNullable(ProjectMemberFactory.createMember()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.deleteManuscript(1L, 2L);
        });

        result.isInstanceOf(CannotDeleteManuscriptException.class);
    }
}
