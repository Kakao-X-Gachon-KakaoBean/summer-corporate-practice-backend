package com.kakaobean.core.project.application;

import com.kakaobean.core.member.domain.Member;
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
import java.util.stream.Collectors;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.project.domain.ProjectRole.*;


@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectValidator projectValidator;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository,
                                MemberRepository memberRepository,
                                ProjectRepository projectRepository,
                                ProjectValidator projectValidator) {
        this.projectMemberRepository = projectMemberRepository;
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.projectValidator = projectValidator;
    }

    @Transactional(readOnly = false)
    public void registerProjectMember(RegisterProjectMemberRequestDto dto){

        //프로젝트를 가져옴.
        Project project = projectRepository.findBySecretKey(dto.getProjectSecretKey())
                .orElseThrow(NotExistsProjectException::new);

        //프로젝트 멤버를 가져온다.
        ProjectMember invitedPerson = projectMemberRepository.findByMemberIdAndProjectId(dto.getMemberId(), project.getId())
                .orElseThrow(NotExistsProjectMemberException::new);

        projectValidator.validInvitedPerson(invitedPerson);
        invitedPerson.modifyProjectRole(VIEWER);
    }

    @Transactional(readOnly = false)
    public ProjectMemberInvitedEvent registerInvitedProjectPersons(InviteProjectMemberRequestDto dto) {
        ProjectMember projectAdmin = projectMemberRepository.findByMemberIdAndProjectId(dto.getProjectAdminId(), dto.getProjectId()).orElseThrow(NotExistsProjectMemberException::new);
        projectValidator.validAdmin(projectAdmin);
        Project project = projectRepository.findProjectById(dto.getProjectId()).orElseThrow(NotExistsProjectException::new);
        List<String> invitedEmails = saveInvitedPersons(dto, project);
        return project.createInvitationProjectMemberEvent(invitedEmails);
    }

    private List<String> saveInvitedPersons(InviteProjectMemberRequestDto dto, Project project) {
        return dto
                .getInvitedMemberIdList()
                .stream()
                .map(invitedMemberId -> saveInvitedPerson(project, invitedMemberId))
                .collect(Collectors.toList());
    }

    private String saveInvitedPerson(Project project, Long invitedMemberId) {
        Member member = memberRepository.findMemberById(invitedMemberId).orElseThrow(NotExistsMemberException::new);
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), INVITED_PERSON));
        return member.getAuth().getEmail();
    }
}
