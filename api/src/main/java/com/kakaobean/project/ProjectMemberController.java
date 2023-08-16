package com.kakaobean.project;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.application.ProjectMemberFacade;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.ModifyProjectMembersRolesRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Timed("api.projectMember")
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
    public ResponseEntity findProjectMembers(@PathVariable Long projectId) {
        log.info("프로젝트 멤버 조회 api 시작");
        List<FindProjectMemberResponseDto> response = projectQueryRepository.findProjectMembers(projectId);
        log.info("프로젝트 멤버 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @PostMapping("/projects/members")
    public ResponseEntity registerProjectMember(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @RequestBody @Validated RegisterProjectMemberRequest request) {
        log.info("프로젝트 멤버 등록 api 시작");
        projectMemberService.registerProjectMember(request.toServiceDto(userPrincipal.getId()));
        log.info("프로젝트 멤버 등록 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 참여에 성공했습니다."), CREATED);
    }

    @PostMapping("/projects/{projectId}/invitation")
    public ResponseEntity inviteProjectMember(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable Long projectId,
                                              @RequestBody InviteProjectMemberRequest request){
        log.info("프로젝트 멤버 초대 api 시작");
        projectMemberFacade.inviteProjectMembers(request.toServiceDto(projectId, userPrincipal.getId()));
        log.info("프로젝트 멤버 초대 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 초대 이메일 발송에 성공했습니다."), OK);
    }

    @PatchMapping("/projects/{projectId}/members")
    public ResponseEntity modifyProjectMemberRole(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable Long projectId,
                                                  @RequestBody @Validated ModifyProjectMembersRolesRequest request) {
        log.info("프로젝트 멤버 권한 수정 api 시작");
        projectMemberService.modifyProjectMemberRole(request.toServiceDto(projectId, userPrincipal.getId()));
        log.info("프로젝트 멤버 권한 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 멤버 권한 수정을 성공했습니다."), OK);
    }
}
