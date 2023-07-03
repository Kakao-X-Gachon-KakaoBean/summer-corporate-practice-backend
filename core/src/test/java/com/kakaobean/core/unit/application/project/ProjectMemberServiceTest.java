package com.kakaobean.core.unit.application.project;


import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ModifyProjectMembersRolesRequestDtoFactory;
import com.kakaobean.core.factory.project.ProjectFactory;

import com.kakaobean.core.member.domain.repository.MemberRepository;

import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.application.dto.request.ModifyProjectMemberRoleRequestDto;
import com.kakaobean.core.project.application.dto.request.ModifyProjectMembersRolesRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.project.exception.NotProjectInvitedPersonException;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.kakaobean.core.factory.project.ModifyProjectMembersRolesRequestDtoFactory.*;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

public class ProjectMemberServiceTest extends UnitTest {

    private ProjectMemberService projectMemberService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    void beforeEach(){
        projectMemberService = new ProjectMemberService(
                projectMemberRepository,
                memberRepository,
                projectRepository,
                new ProjectValidator()
        );
    }


    @Test
    void 초대_이메일을_받은_회원이_프로젝트에_참여한다(){
        //given
        ProjectMember invitedPerson = createInvitedPerson();
        given(projectRepository.findBySecretKey(Mockito.anyString()))
                .willReturn(Optional.of(ProjectFactory.create()));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(invitedPerson));

        //when
        projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto("secretKey", 1L));

        //then
        assertThat(invitedPerson.getProjectRole()).isEqualTo(VIEWER);
    }

    @Test
    void 초대_받은_신분의_회원만_프로젝트에_참가할_수_있다(){
        //given
        given(projectRepository.findBySecretKey(Mockito.anyString()))
                .willReturn(Optional.of(ProjectFactory.create()));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto("secretKey", 1L));
        });

        //then
        result.isInstanceOf(NotProjectInvitedPersonException.class);
    }

    @Test
    void 관리자가_프로젝트_멤버_초대_이메일_전송_도메인_이벤트를_만든다(){
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));
        given(projectRepository.findProjectById(Mockito.anyLong())).willReturn(Optional.of(ProjectFactory.create()));
        given(memberRepository.findMemberById(Mockito.anyLong())).willReturn(Optional.of(MemberFactory.create()));

        //when
        ProjectMemberInvitedEvent event = projectMemberService.registerInvitedProjectPersons(new InviteProjectMemberRequestDto(List.of(1L, 2L), 3L, 4L));

        //then
        assertThat(event.getInvitedMemberEmails().size()).isEqualTo(2);
        verify(projectMemberRepository, times(2)).save(Mockito.any(ProjectMember.class));
    }

    @Test
    void 일반_멤버가_프로젝트_멤버_초대_이메일_전송_도메인_이벤트를_만드는_것을_실패한다(){
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.registerInvitedProjectPersons(new InviteProjectMemberRequestDto(List.of(1L, 2L), 3L, 4L));
        });

        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }

    @Test
    void 관리자가_프로젝트_멤버들_권한을_변경한다(){
        //given
        ProjectMember member1 = createMember();
        ProjectMember member2 = createMember();

        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createAdmin()), Optional.of(member1), Optional.of(member2));


        //when
        projectMemberService.modifyProjectMemberRole(create());

        //when
        assertThat(member1.getProjectRole()).isSameAs(ADMIN);
        assertThat(member2.getProjectRole()).isSameAs(VIEWER);
    }

    @Test
    void 관리자가_아니면_멤버_권한을_수정할_수_없다(){
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.modifyProjectMemberRole(create());
        });

        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }
}
