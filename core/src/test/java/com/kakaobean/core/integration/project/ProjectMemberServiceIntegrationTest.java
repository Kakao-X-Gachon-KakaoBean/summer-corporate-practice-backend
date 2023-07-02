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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProjectMemberServiceIntegrationTest extends IntegrationTest {

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
        Member member = memberRepository.save(MemberFactory.create());
        Member invitedMember = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), ADMIN));

        //when
        ProjectMemberInvitedEvent event = projectMemberService.registerInvitedProjectPersons(new InviteProjectMemberRequestDto(List.of(invitedMember.getId()), project.getId(), member.getId()));

        //then
        assertThat(event.getProject()).isSameAs(project);
        assertThat(event.getInvitedMemberEmails().size()).isEqualTo(1);
        assertThat(event.getInvitedMemberEmails().get(0)).isEqualTo(invitedMember.getAuth().getEmail());
    }

    @Test
    void 일반_멤버가_프로젝트_멤버_초대_이메일_전송_도메인_이벤트를_만드는_것을_실패한다(){
        //given
        Member member = memberRepository.save(MemberFactory.create());
        Member invitedMember = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), MEMBER));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.registerInvitedProjectPersons(new InviteProjectMemberRequestDto(List.of(invitedMember.getId()), project.getId(), member.getId()));
        });

        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }

    @Test
    void 초대_받은_회원이_프로젝트에_참여한다(){
        //given
        Member member = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(ProjectMemberFactory.createWithMemberIdAndProjectId(member.getId(), project.getId(), INVITED_PERSON));

        //when
        projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto(project.getSecretKey(), member.getId()));

        //then
        assertThat(projectMemberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 초대_받은_신분의_회원만_프로젝트에_참가할_수_있다(){
        //given
        Member member = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(ProjectMemberFactory.createWithMemberIdAndProjectId(member.getId(), project.getId(), MEMBER));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto(project.getSecretKey(), member.getId()));
        });

        //then
        result.isInstanceOf(NotProjectInvitedPersonException.class);
    }
}
