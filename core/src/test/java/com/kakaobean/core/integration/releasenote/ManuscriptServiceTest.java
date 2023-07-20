package com.kakaobean.core.integration.releasenote;

import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.factory.releasenote.ManuscriptFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.request.ModifyManuscriptRequestDto;
import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.exception.*;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.kakaobean.core.project.domain.ProjectRole.*;
import static com.kakaobean.core.releasenote.domain.ManuscriptStatus.Modifiable;
import static com.kakaobean.core.releasenote.domain.ManuscriptStatus.Modifying;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ManuscriptServiceTest extends IntegrationTest {

    @Autowired
    ManuscriptService manuscriptService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @Test
    void 릴리즈_노트_첫_원고를_등록한다() {

        //given
        Project project = projectRepository
                .save(ProjectFactory.create());
        ProjectMember admin = projectMemberRepository
                .save(ProjectMemberFactory.createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        RegisterManuscriptRequestDto dto =
                new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.1", admin.getMemberId(), project.getId());

        //when
        manuscriptService.registerManuscript(dto);

        //then
        assertThat(manuscriptRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 관리자만_릴리즈_노트_첫_원고를_등록할_수_있다() {

        Project project = projectRepository
                .save(ProjectFactory.create());
        ProjectMember admin = projectMemberRepository
                .save(ProjectMemberFactory.createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        RegisterManuscriptRequestDto dto =
                new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.1", admin.getMemberId(), project.getId());

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.registerManuscript(dto);
        });

        result.isInstanceOf(ManuscriptWriterAccessException.class);
    }

    @Test
    void 중복되는_릴리즈_노트_원고_버전이_존재한다() {
        Project project = projectRepository
                .save(ProjectFactory.create());
        ProjectMember admin = projectMemberRepository
                .save(ProjectMemberFactory.createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        RegisterManuscriptRequestDto dto =
                new RegisterManuscriptRequestDto("3.1 코코노트 배포", "내용..", "3.1", admin.getMemberId(), project.getId());
        manuscriptRepository.save(ManuscriptFactory.createWithId(admin.getMemberId(), project.getId()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.registerManuscript(dto);
        });

        result.isInstanceOf(DuplicateManuscriptVersionException.class);
    }

    @Test
    void 릴리즈_노트_원고의_수정_권한을_얻는다() {

        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifiable);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, ADMIN);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);

        manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());

        assertThat(manuscript.getManuscriptStatus()).isSameAs(Modifying);
    }

    @Test
    void 뷰어는_릴리즈_노트_원고를_수정_권한을_얻을_수_없다() {

        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifiable);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, VIEWER);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);


        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());
        });

        result.isInstanceOf(ManuscriptModificationAccessException.class);
    }

    @Test
    void 다른_멤버가_릴리즈_노트_원고를_수정_중이면_원고를_수정할_권한을_얻을_수_없다() {
        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifying);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, MEMBER);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());
        });

        result.isInstanceOf(AnotherMemberAlreadyModifyingException.class);
    }


    @Test
    void 릴리즈_노트_원고를_수정한다() {

        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifying);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, ADMIN);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);
        ModifyManuscriptRequestDto dto = ManuscriptFactory.createServiceDto(projectMember.getMemberId(), manuscript.getId());

        manuscriptService.modifyManuscript(dto);

        assertThat(manuscript.getManuscriptStatus()).isSameAs(Modifiable);
        assertThat(manuscript.getTitle()).isEqualTo(dto.getTitle());
        assertThat(manuscript.getContent()).isEqualTo(dto.getContent());
        assertThat(manuscript.getVersion()).isEqualTo(dto.getVersion());
        assertThat(manuscript.getLastEditedMemberId()).isSameAs(dto.getEditingMemberId());
    }

    @Test
    void 뷰어는_릴리즈_노트_원고를_수정할_수_없다() {

        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifiable);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, VIEWER);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);
        ModifyManuscriptRequestDto dto = ManuscriptFactory.createServiceDto(projectMember.getMemberId(), manuscript.getId());


        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.modifyManuscript(dto);
        });

        result.isInstanceOf(ManuscriptModificationAccessException.class);
    }

    @Test
    void 릴리즈_노트_원고_상태가_수정중이_아니면_수정할_수_없다() {
        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifiable);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, MEMBER);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);
        ModifyManuscriptRequestDto dto = ManuscriptFactory.createServiceDto(projectMember.getMemberId(), manuscript.getId());

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.modifyManuscript(dto);
        });

        result.isInstanceOf(CannotModifyManuscriptException.class);
    }

    @Test
    void 릴리즈_노트_원고를_삭제한다() {
        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifiable);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, ADMIN);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);

        manuscriptService.deleteManuscript(projectMember.getMemberId(), manuscript.getId());

        assertThat(manuscriptRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void 릴리즈_노트는_관리자가_아니라면_삭제할_수_없다() {
        Manuscript manuscript = ManuscriptFactory.createWithId(1L, 2L, Modifiable);
        ProjectMember projectMember = ProjectMemberFactory.createWithMemberIdAndProjectId(1L, 2L, MEMBER);
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.deleteManuscript(projectMember.getMemberId(), manuscript.getId());
        });

        result.isInstanceOf(CannotDeleteManuscriptException.class);
    }
}
