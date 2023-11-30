package com.kakaobean.project;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.domain.repository.query.*;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.project.dto.request.ModifyProjectRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Timed("api.project")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectQueryRepository projectQueryRepository;

    @PostMapping("/projects")
    public ResponseEntity registerProject(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @Validated @RequestBody RegisterProjectRequest request) {
        log.info("프로젝트 등록 api 시작");
        RegisterProjectResponseDto res = projectService.registerProject(request.toServiceDto(userPrincipal.getId()));
        log.info("프로젝트 등록 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from(res.getProjectId(), "프로젝트가 생성 되었습니다."), CREATED);
    }

    @GetMapping("/projects")
    public ResponseEntity findParticipatedProjects(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("멤버 참여 프로젝트 조회 api 시작");
        FindProjectsResponseDto response = projectQueryRepository.findProjects(userPrincipal.getId());
        log.info("멤버 참여 프로젝트 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @PatchMapping("/projects/{projectId}")
    public ResponseEntity modifyProject(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @PathVariable Long projectId,
                                        @Validated @RequestBody ModifyProjectRequest request) {
        log.info("프로젝트 수정 api 시작");
        projectService.modifyProject(request.toServiceDto(userPrincipal.getId(), projectId));
        log.info("프로젝트 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트 정보가 변경 되었습니다."), OK);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity removeProject(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @PathVariable Long projectId) {
        log.info("프로젝트 제거 api 시작");
        projectService.removeProject(userPrincipal.getId(), projectId);
        log.info("프로젝트 제거 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("프로젝트가 삭제 되었습니다."), OK);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity findProjectMembers(@PathVariable Long projectId){
        log.info("프로젝트 멤버 조회 api 시작");
        FindProjectInfoResponseDto response = projectQueryRepository.findProject(projectId);
        log.info("프로젝트 멤버 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/projects/title")
    public ResponseEntity findProjectTitle(@RequestParam String projectSecretKey){
        log.info("비밀 키를 사용한 프로젝트 제목 조회 api 시작");
        FindProjectTitleResponseDto response = projectQueryRepository.findBySecretKey(projectSecretKey);
        log.info("비밀 키를 사용한 프로젝트 제목 조회 api 종료");
        return new ResponseEntity(response,OK);
    }
}
