package com.kakaobean.core.integration.project;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.integration.IntegrationTest;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.project.exception.NotProjectInvitedPersonException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.factory.project.dto.ModifyProjectMembersRolesRequestDtoFactory.create;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProjectMemberServiceTest extends IntegrationTest {

    @Autowired
    ProjectMemberService projectMemberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;


    @Test
    void 관리자가_프로젝트_멤버_초대_이메일_전송_도메인_이벤트를_만든다(){
        //given
        Member member = memberRepository.save(MemberFactory.createWithoutId());
        Member invitedMember = memberRepository.save(MemberFactory.createWithoutId());
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), ADMIN));

        //when
        ProjectMemberInvitedEvent event = projectMemberService.registerInvitedProjectPersons(new InviteProjectMemberRequestDto(List.of(invitedMember.getAuth().getEmail()), project.getId(), member.getId()));

        //then
        assertThat(event.getProject().getId()).isEqualTo(project.getId());
        assertThat(event.getInvitedMemberEmails().size()).isEqualTo(1);
        assertThat(event.getInvitedMemberEmails().get(0)).isEqualTo(invitedMember.getAuth().getEmail());
    }

    @Test
    void 일반_멤버가_프로젝트_멤버_초대_이메일_전송_도메인_이벤트를_만드는_것을_실패한다(){
        //given
        Member member = memberRepository.save(MemberFactory.create());
        Member invitedMember = memberRepository.save(MemberFactory.createWithoutId());
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), MEMBER));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.registerInvitedProjectPersons(new InviteProjectMemberRequestDto(List.of(invitedMember.getAuth().getEmail()), project.getId(), member.getId()));
        });

        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }

    @Test
    void 초대_받은_회원이_프로젝트에_참여한다(){
        //given
        Member member = memberRepository.save(MemberFactory.createWithoutId());
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        projectMemberRepository.save(ProjectMemberFactory.createWithMemberIdAndProjectId(member.getId(), project.getId(), INVITED_PERSON));

        //when
        projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto(project.getSecretKey(), member.getId()));

        //then
        assertThat(projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId()).isPresent()).isTrue();
    }

    @Test
    void 초대_받은_신분의_회원만_프로젝트에_참가할_수_있다(){
        //given
        Member member = memberRepository.save(MemberFactory.createWithoutId());
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        projectMemberRepository.save(ProjectMemberFactory.createWithMemberIdAndProjectId(member.getId(), project.getId(), MEMBER));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto(project.getSecretKey(), member.getId()));
        });

        //then
        result.isInstanceOf(NotProjectInvitedPersonException.class);
    }

    @Test
    void 관리자가_프로젝트_멤버들_권한을_변경한다(){
        //given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());

        ProjectMember admin = createWithProjectId(project.getId(), ADMIN);
        ProjectMember member1 = createWithProjectId(project.getId(), MEMBER);
        ProjectMember member2 = createWithProjectId(project.getId(), MEMBER);

        projectMemberRepository.save(admin);
        projectMemberRepository.save(member1);
        projectMemberRepository.save(member2);

        //when
        projectMemberService.modifyProjectMemberRole(create(admin.getMemberId(), project.getId(), member1.getMemberId(), member2.getMemberId()));

        //when
        assertThat(projectMemberRepository.findByMemberIdAndProjectId(member1.getMemberId(), member1.getProjectId()).get().getProjectRole())
                .isSameAs(ADMIN);
        assertThat(projectMemberRepository.findByMemberIdAndProjectId(member2.getMemberId(), member2.getProjectId()).get().getProjectRole())
                .isSameAs(VIEWER);
    }

    @Test
    void 관리자가_아니면_멤버_권한을_수정할_수_없다(){
        //given
        Project project = projectRepository.save(ProjectFactory.create());

        ProjectMember admin = createWithProjectId(project.getId(), MEMBER);
        ProjectMember member1 = createWithProjectId(project.getId(), MEMBER);
        ProjectMember member2 = createWithProjectId(project.getId(), MEMBER);

        ProjectMember savedAdmin = projectMemberRepository.save(admin);
        ProjectMember savedMember1 = projectMemberRepository.save(member1);
        ProjectMember savedMember2 = projectMemberRepository.save(member2);

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.modifyProjectMemberRole(create(savedAdmin.getMemberId(), project.getId(), savedMember1.getMemberId(), savedMember2.getMemberId()));
        });

        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }
}
