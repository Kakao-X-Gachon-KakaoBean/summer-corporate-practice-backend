package com.kakaobean.core.project.application;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.application.dto.request.ModifyProjectMemberRoleRequestDto;
import com.kakaobean.core.project.application.dto.request.ModifyProjectMembersRolesRequestDto;
import com.kakaobean.core.project.application.dto.request.RegisterProjectMemberRequestDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static com.kakaobean.core.project.domain.ProjectRole.*;


@Service
@Transactional(readOnly = false)
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

    @CacheEvict(cacheNames = "projectCache", key = "#dto.memberId")
    public void registerProjectMember(RegisterProjectMemberRequestDto dto){

        //프로젝트를 가져옴.
        Project project = projectRepository.findBySecretKey(dto.getProjectSecretKey())
                .orElseThrow(NotExistsProjectException::new);

        //프로젝트 멤버를 가져온다.
        ProjectMember invitedPerson = projectMemberRepository.findByMemberIdAndProjectId(dto.getMemberId(), project.getId())
                .orElseThrow(NotExistsProjectMemberException::new);

        projectValidator.validInvitedPerson(invitedPerson);
        invitedPerson.modifyProjectRole(VIEWER);
        invitedPerson.registered();
    }

    public ProjectMemberInvitedEvent registerInvitedProjectPersons(InviteProjectMemberRequestDto dto) {

        //관리자인지 검증
        projectValidator.validAdmin(dto.getProjectAdminId(), dto.getProjectId());

        //프로젝트를 찾고
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(NotExistsProjectException::new);

        //초대 멤버로 저장하고 도메인 이벤트를 발행
        List<String> invitedEmails = saveInvitedPersons(dto, project);
        return project.createInvitationProjectMemberEvent(invitedEmails);
    }

    private List<String> saveInvitedPersons(InviteProjectMemberRequestDto dto, Project project) {
        return dto
                .getInvitedMemberEmails()
                .stream()
                .map(email -> saveInvitedPerson(project, email))
                .collect(Collectors.toList());
    }

    private String saveInvitedPerson(Project project, String email) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(NotExistsMemberException::new);
        projectMemberRepository.save(new ProjectMember(ACTIVE, project.getId(), member.getId(), INVITED_PERSON));
        return member.getAuth().getEmail();
    }

    public void modifyProjectMemberRole(ModifyProjectMembersRolesRequestDto dto){
        projectValidator.validAdmin(dto.getAdminId(), dto.getProjectId());
        modifyProjectMembersRoles(dto.getProjectId(), dto.getModifyProjectMemberRoles());
    }

    private void modifyProjectMembersRoles(Long projectId, List<ModifyProjectMemberRoleRequestDto> dto) {
        for (ModifyProjectMemberRoleRequestDto modifyProjectMemberRole : dto) {
            ProjectMember projectMember = projectMemberRepository
                    .findByMemberIdAndProjectId(modifyProjectMemberRole.getModifyProjectMemberId(), projectId)
                    .orElseThrow(NotExistsProjectMemberException::new);
            projectMember.modifyProjectRole(modifyProjectMemberRole.getProjectRole());
        }
    }
}
