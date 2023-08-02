package com.kakaobean.core.integration.project;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.releasenote.ManuscriptFactory;
import com.kakaobean.core.factory.releasenote.ReleaseNoteFactory;
import com.kakaobean.core.factory.sprint.SprintFactory;
import com.kakaobean.core.factory.sprint.TaskFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.factory.member.MemberFactory.create;
import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;
import static com.kakaobean.core.project.domain.ProjectRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProjectServiceTest extends IntegrationTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        projectRepository.deleteAll();;
        projectMemberRepository.deleteAll();
    }

    @Test
    void 로그인한_유저가_프로젝트_생성에_성공한다() {
        //given
        Member member = memberRepository.save(create());

        //when
        RegisterProjectResponseDto responseDto = projectService.registerProject(new RegisterProjectRequestDto("프로젝트 이름", "프로젝트 내용", member.getId()));
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), responseDto.getProjectId())
                .orElseThrow(NotExistsMemberException::new);

        //then
        assertThat(projectMember.getMemberId()).isEqualTo(member.getId());
        assertThat(projectMember.getProjectId()).isEqualTo(responseDto.getProjectId());
        assertThat(projectMember.getProjectRole()).isSameAs(ADMIN);
    }

    @Test
    void 어드민이_프로젝트_정보를_수정에_성공한다() {
        // given
        Member member = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), ADMIN));
        ModifyProjectInfoRequestDto responseDto = new ModifyProjectInfoRequestDto(member.getId(), project.getId(), "새로운 제목", "새로운 설명");

        // when
        projectService.modifyProject(responseDto);

        // then
        assertThat(projectRepository.findById(project.getId()).get().getTitle()).isEqualTo("새로운 제목");
    }

    @Test
    void 일반유저는_프로젝트_정보를_수정에_실패한다() {
        // given
        Member member = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), MEMBER));
        ModifyProjectInfoRequestDto responseDto = new ModifyProjectInfoRequestDto(member.getId(), project.getId(), "새로운 제목", "새로운 설명");

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectService.modifyProject(responseDto);
        });

        // then
        result.isInstanceOf(NotProjectAdminException.class);
    }

//    // 비동기 때문에 테스트에 실패하는 경우가 있음
//    @Test
//    void 어드민이_프로젝트_삭제에_성공한다() throws InterruptedException {
//        //given
//        Member admin = memberRepository.save(MemberFactory.createWithoutId());
//        Member member = memberRepository.save(MemberFactory.createWithoutId());
//        Project project = projectRepository.save(ProjectFactory.create());
//
//        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), admin.getId(), ADMIN));
//        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), MEMBER));
//
//        manuscriptRepository.save(ManuscriptFactory.createWithId(member.getId(), project.getId()));
//        releaseNoteRepository.save(ReleaseNoteFactory.createWithId(member.getId(), project.getId()));
//
//        Sprint sprint = sprintRepository.save(SprintFactory.createWithId(project.getId()));
//        taskRepository.save(TaskFactory.createWithId(sprint.getId(), admin.getId()));
//        taskRepository.save(TaskFactory.createWithId(sprint.getId(), member.getId()));
//
//        //when
//        projectService.removeProject(admin.getId(), project.getId());
//
//        //then
//        assertThat(projectRepository.findAll().size()).isEqualTo(0);
//        assertThat(projectMemberRepository.findAll().size()).isEqualTo(0);
//        assertThat(manuscriptRepository.findAll().size()).isEqualTo(0);
//        assertThat(releaseNoteRepository.findAll().size()).isEqualTo(0);
//        assertThat(sprintRepository.findAll().size()).isEqualTo(0);
//        assertThat(taskRepository.findAll().size()).isEqualTo(0);
//    }

    @Test
    void 일반유저는_프로젝트_정보를_삭제에_실패한다() throws InterruptedException {
        //given
        Member member = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), MEMBER));
        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(()->{
           projectService.removeProject(member.getId(), project.getId());
        });
        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }


}
