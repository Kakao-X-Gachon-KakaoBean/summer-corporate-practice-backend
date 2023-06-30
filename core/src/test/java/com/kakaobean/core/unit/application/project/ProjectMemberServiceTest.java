package com.kakaobean.core.unit.application.project;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.member.RegisterMemberServiceDtoFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.member.application.MemberService;
import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.core.member.domain.*;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.domain.service.VerifiedEmailService;
import com.kakaobean.core.member.exception.member.*;
import com.kakaobean.core.member.infrastructure.MemberVerifiedEmailServiceImpl;
import com.kakaobean.core.member.infrastructure.ModifyMemberServiceImpl;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import com.kakaobean.core.project.infrastructure.InvitationProjectMemberServiceImpl;
import com.kakaobean.core.unit.UnitTest;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
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

    @Mock
    private EmailSender emailSender;

    @BeforeEach
    void beforeEach(){
        projectMemberService = new ProjectMemberService(
                projectMemberRepository,
                memberRepository,
                projectRepository,
                new ProjectValidator(),
                new InvitationProjectMemberServiceImpl(memberRepository, emailSender)
        );
    }

    @Test
    void 관리자가_프로젝트_참여_멤버를_초대한다(){
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createAdmin()));
        given(projectRepository.findProjectById(Mockito.anyLong())).willReturn(Optional.of(ProjectFactory.create()));

        //when
        List<ProjectMemberInvitedEvent> events = projectMemberService.inviteProjectMembers(new InviteProjectMemberRequestDto(List.of(1L, 2L), 3L, 4L));

        //then
        Assertions.assertThat(events.size()).isEqualTo(2);
        verify(projectMemberRepository, times(1)).findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void 일반_프로젝트_멤버가_프로젝트_참여_멤버를_초대해_테스트가_실패한다(){
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).willReturn(Optional.of(createMember()));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            projectMemberService.inviteProjectMembers(new InviteProjectMemberRequestDto(List.of(1L, 2L), 3L, 4L));
        });

        //then
        result.isInstanceOf(NotProjectAdminException.class);
    }

    @Test
    void 프로젝트에_참여한다(){
        //given
        given(projectRepository.findBySecretKey(Mockito.anyString())).willReturn(Optional.of(ProjectFactory.create()));
        given(memberRepository.findMemberById(Mockito.anyLong())).willReturn(Optional.of(MemberFactory.create()));

        //when
        projectMemberService.registerProjectMember(new RegisterProjectMemberRequestDto("secretKey", 1L));

        //then
        verify(projectMemberRepository, times(1)).save(Mockito.any(ProjectMember.class));
    }
}
