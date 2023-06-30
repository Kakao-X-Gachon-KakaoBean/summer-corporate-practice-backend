package com.kakaobean.core.project.application;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.MemberValidator;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
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

    public void registerProjectMember(Long memberId, String projectSecretKey){

        //TODO 멤버가 존재하는지 검증

        //TODO 프로젝트를 찾아야함

        //ProjectMember projectMember = new ProjectMember(ACTIVE,   );
        //projectMemberRepository.save(projectMember)

    }

    @Transactional(readOnly = false)
    public void inviteProjectMembers(Long projectAdminId,
                                     Long projectId,
                                     InviteProjectMemberRequestDto inviteProjectMemberRequestDto) {
        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(projectAdminId, projectId).orElseThrow();
        projectValidator.validAdmin(projectAdmin);
        Project project = projectRepository.findProjectById(projectId).orElseThrow();
        invitationProjectMemberService.sendInvitationMails(inviteProjectMemberRequestDto.getInvitedMemberIdList(), project);
    }
}
