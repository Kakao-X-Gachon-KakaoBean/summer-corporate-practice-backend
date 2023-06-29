package com.kakaobean.project;

import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @PostMapping("/projects/{secret}/members")
    public ResponseEntity registerProjectMember(@AuthenticationPrincipal Long id, @PathVariable Long secret){

    }

    @PostMapping("/projects/invitation/{projectId}")
    public ResponseEntity inviteProjectMember(@AuthenticationPrincipal Long projectAdminId,
                                              @PathVariable Long projectId,
                                              @RequestBody InviteProjectMemberRequest request){
        projectMemberService.inviteMember(projectAdminId, projectId, request.toServiceDto());
    }
}
