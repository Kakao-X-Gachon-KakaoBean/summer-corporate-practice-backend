package com.kakaobean.core.project.application;

import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;


@Service
@Transactional(readOnly = false)
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectValidator projectValidator;
    private final InvitationProjectMemberService invitationProjectMemberService;


    public ProjectMemberService(ProjectMemberRepository projectMemberRepository,
                                MemberRepository memberRepository,
                                ProjectRepository projectRepository,
                                ProjectValidator projectValidator,
                                InvitationProjectMemberService invitationProjectMemberService) {
        this.projectMemberRepository = projectMemberRepository;
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.projectValidator = projectValidator;
        this.invitationProjectMemberService = invitationProjectMemberService;
    }

    public void registerProjectMember(RegisterProjectMemberRequestDto dto){
        memberRepository.findMemberById(dto.getMemberId()).orElseThrow();
        Project project = projectRepository.findBySecretKey(dto.getProjectSecretKey()).orElseThrow();
        ProjectMember projectMember = new ProjectMember(ACTIVE, project.getId(), dto.getMemberId(), ProjectRole.VIEWER);
        projectMemberRepository.save(projectMember);
    }

    public void inviteProjectMembers(Long projectAdminId,
                                     Long projectId,
                                     InviteProjectMemberRequestDto inviteProjectMemberRequestDto) {
        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(projectAdminId, projectId).orElseThrow();
        projectValidator.validAdmin(projectAdmin);
        Project project = projectRepository.findProjectById(projectId).orElseThrow();
        invitationProjectMemberService.sendInvitationMails(inviteProjectMemberRequestDto.getInvitedMemberIdList(), project);
    }
}
