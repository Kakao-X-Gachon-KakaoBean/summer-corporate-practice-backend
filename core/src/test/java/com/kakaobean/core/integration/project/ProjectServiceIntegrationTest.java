package com.kakaobean.core.integration.project;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ModifyProjectMembersRolesRequestDtoFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.application.dto.request.ModifyProjectInfoReqeustDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotProjectAdminException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.factory.member.MemberFactory.create;
import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;
import static com.kakaobean.core.project.domain.ProjectRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ProjectServiceIntegrationTest extends IntegrationTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

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


}
