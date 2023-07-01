package com.kakaobean.project;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.application.ProjectMemberFacade;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;
    private final ProjectMemberFacade projectMemberFacade;
    private final ProjectQueryRepository projectQueryRepository;

    public ProjectMemberController(ProjectMemberService projectMemberService,
                                   ProjectMemberFacade projectMemberFacade,
                                   ProjectQueryRepository projectQueryRepository) {
        this.projectMemberService = projectMemberService;
        this.projectMemberFacade = projectMemberFacade;
        this.projectQueryRepository = projectQueryRepository;
    }

    @GetMapping("/projects/{projectId}/members")
    public ResponseEntity findProjectMembers(@PathVariable Long projectId){
        List<FindProjectMemberResponseDto> response = projectQueryRepository.findProjectMembers(projectId);
        return new ResponseEntity(response, OK);
    }

    @PostMapping("/projects/members")
    public ResponseEntity registerProjectMember(@AuthenticationPrincipal Long id, @RequestBody RegisterProjectMemberRequest request){
        projectMemberService.registerProjectMember(request.toServiceDto(id));
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 참여에 성공했습니다."), CREATED);
    }

    @PostMapping("/projects/{projectId}/invitation")
    public ResponseEntity inviteProjectMember(@AuthenticationPrincipal Long projectAdminId,
                                              @PathVariable Long projectId,
                                              @RequestBody InviteProjectMemberRequest request){
        projectMemberFacade.inviteProjectMembers(request.toServiceDto(projectId, projectAdminId));
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 초대 이메일 발송에 성공했습니다."), OK);
    }
}
