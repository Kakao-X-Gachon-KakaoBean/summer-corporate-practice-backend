package com.kakaobean.core.project.application;

import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;


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

    @Transactional(readOnly = false)
    public void registerProjectMember(RegisterProjectMemberRequestDto dto){
        memberRepository.findMemberById(dto.getMemberId()).orElseThrow(NotExistsMemberException::new);
        Project project = projectRepository.findBySecretKey(dto.getProjectSecretKey()).orElseThrow(NotExistsProjectException::new);
        ProjectMember projectMember = new ProjectMember(ACTIVE, project.getId(), dto.getMemberId(), ProjectRole.VIEWER);
        projectMemberRepository.save(projectMember);
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberInvitedEvent> inviteProjectMembers(InviteProjectMemberRequestDto dto) {
        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(dto.getProjectAdminId(), dto.getProjectId()).orElseThrow(NotExistsProjectMemberException::new);
        projectValidator.validAdmin(projectAdmin);
        Project project = projectRepository.findProjectById(dto.getProjectId()).orElseThrow(NotExistsProjectException::new);
        return project.sendInvitationEmail(dto.getInvitedMemberIdList());
    }
}
