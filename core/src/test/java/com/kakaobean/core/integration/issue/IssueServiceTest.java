package com.kakaobean.core.integration.issue;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
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
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.factory.member.MemberFactory.create;
import static org.assertj.core.api.Assertions.assertThat;

public class IssueServiceTest extends IntegrationTest {

    @Autowired
    IssueService issueService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ReleaseNoteRepository releaseNoteRepository;

    @Test
    void 로그인한_유저가_프로젝트_생성에_성공한다() {
        //given
        Member member = memberRepository.save(create());

        //when
        RegisterIssueResponseDto responseDto = issueService.registerIssue(new RegisterIssueRequestDto(1L, "프로젝트 이름", "프로젝트 내용", member.getId()));
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), responseDto.getIssueId())
                .orElseThrow(NotExistsMemberException::new);

        //then
        assertThat(projectMember.getMemberId()).isEqualTo(member.getId());
//        assertThat(projectMember.getProjectId()).isEqualTo(responseDto.getProjectId());
    }
}
