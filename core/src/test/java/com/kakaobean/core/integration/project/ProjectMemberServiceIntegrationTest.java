package com.kakaobean.core.integration.project;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.member.RegisterMemberServiceDtoFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.core.member.domain.Auth;
import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Email;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.*;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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
    void 프로젝트_멤버_초대를_성공한다(){
        //given
        Member member = memberRepository.save(MemberFactory.create());
        Member invitedMember = memberRepository.save(MemberFactory.create());
        Project project = projectRepository.save(ProjectFactory.create());
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), ADMIN));

        //when
        projectMemberService.inviteProjectMembers(new InviteProjectMemberRequestDto(List.of(invitedMember.getId()), project.getId(), member.getId()));

        //then
    }
}
