package com.kakaobean.project;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @PostMapping("/projects/members")
    public ResponseEntity registerProjectMember(@AuthenticationPrincipal Long id, @RequestBody RegisterProjectMemberRequest request){
        projectMemberService.registerProjectMember(request.toServiceDto(id));
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 참여에 성공했습니다."), HttpStatus.CREATED);
    }

    @PostMapping("/projects/{projectId}/invitation")
    public ResponseEntity inviteProjectMember(@AuthenticationPrincipal Long projectAdminId,
                                              @PathVariable Long projectId,
                                              @RequestBody InviteProjectMemberRequest request){
        projectMemberService.inviteProjectMembers(request.toServiceDto(projectId, projectAdminId));
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 초대 이메일 발송에 성공했습니다."), HttpStatus.OK);
    }
}
