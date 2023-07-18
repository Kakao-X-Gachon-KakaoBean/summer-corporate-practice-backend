package com.kakaobean.core.integration.releasenote;

import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptRepository;
import com.kakaobean.core.releasenote.exception.DuplicateManuscriptVersionException;
import com.kakaobean.core.releasenote.exception.ManuscriptWriterAccessException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        manuscriptRepository.save(new Manuscript(ACTIVE, "title", "c", "3.1", admin.getMemberId(), project.getId()));

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            manuscriptService.registerManuscript(dto);
        });

        result.isInstanceOf(DuplicateManuscriptVersionException.class);
    }
}
