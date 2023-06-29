package com.kakaobean.core.project.application;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;

import static com.kakaobean.core.common.domain.BaseStatus.*;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;


    public ProjectMemberService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    public void registerProjectMember(Long memberId, String projectSecretKey){

        //TODO 멤버가 존재하는지 검증

        //TODO 프로젝트를 찾아야함

        ProjectMember projectMember = new ProjectMember(ACTIVE,   );
        projectMemberRepository.save(projectMember)

    }

    public void inviteMember(Long projectAdminId,
                             Long projectId,
                             InviteProjectMemberRequestDto inviteProjectMemberRequestDto) {

        //프로젝트 아이디와 멤버 아이디를 기준으로 관리자인지 검증함.


        //모든 멤버가 존재하는지 검증함


        //모든 멤버들에게 이메일을 보냄.


    }
}
