package com.kakaobean.project;

import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectQueryRepository projectQueryRepository;

    @PostMapping("/projects")
    public ResponseEntity registerProject(@AuthenticationPrincipal Long memberId,
                                          @Validated @RequestBody RegisterProjectRequest request) {
        log.info("프로젝트 생성 api 호출");
        RegisterProjectResponseDto res = projectService.registerProject(request.toServiceDto(memberId));
        log.info("프로젝트 생성 api 호출");
        return new ResponseEntity(res, OK);
    }

    @GetMapping("/projects")
    public ResponseEntity findParticipatedProjects(@AuthenticationPrincipal Long id){
        List<FindProjectResponseDto> response = projectQueryRepository.findProjects(id);
        return new ResponseEntity(response, OK);
    }
}
