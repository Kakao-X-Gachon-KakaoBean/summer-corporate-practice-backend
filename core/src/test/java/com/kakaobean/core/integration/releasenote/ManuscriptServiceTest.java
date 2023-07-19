package com.kakaobean.core.integration.releasenote;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.factory.releasenote.ManuscriptFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.exception.AnotherMemberAlreadyModifyingException;
import com.kakaobean.core.releasenote.exception.DuplicateManuscriptVersionException;
import com.kakaobean.core.releasenote.exception.ManuscriptModificationAccessException;
import com.kakaobean.core.releasenote.exception.ManuscriptWriterAccessException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.releasenote.domain.ManuscriptStatus.Modifying;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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
                .save(ProjectMemberFactory.createWithMemberIdAndProjectId(1L, project.getId(), ProjectRole.ADMIN));
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
                .save(ProjectMemberFactory.createWithMemberIdAndProjectId(1L, project.getId(), ProjectRole.MEMBER));
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
                .save(ProjectMemberFactory.createWithMemberIdAndProjectId(1L, project.getId(), ProjectRole.ADMIN));
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

        Manuscript manuscript = ManuscriptFactory.create();
        ProjectMember projectMember = ProjectMemberFactory.createAdmin();
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);

        manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());

        assertThat(manuscript.getManuscriptStatus()).isSameAs(Modifying);
    }

    @Test
    void 뷰어는_릴리즈_노트_원고를_수정_권한을_얻을_수_없다() {

        Manuscript manuscript = ManuscriptFactory.create();
        ProjectMember projectMember = ProjectMemberFactory.createViewer();
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);


        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());
        });

        result.isInstanceOf(ManuscriptModificationAccessException.class);
    }

    @Test
    void 다른_멤버가_릴리즈_노트_원고를_수정_중이면_원고를_수정할_권한을_얻을_수_없다() {
        Manuscript manuscript = ManuscriptFactory.create();
        ProjectMember projectMember = ProjectMemberFactory.createAdmin();
        manuscriptRepository.save(manuscript);
        projectMemberRepository.save(projectMember);

        manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.hasRightToModifyManuscript(projectMember.getMemberId(), manuscript.getId());
        });

        result.isInstanceOf(AnotherMemberAlreadyModifyingException.class);
    }
}
