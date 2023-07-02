package com.kakaobean.project;

import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.security.UserPrincipal;
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
    public ResponseEntity registerProject(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @Validated @RequestBody RegisterProjectRequest request) {
        RegisterProjectResponseDto res = projectService.registerProject(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(res, CREATED);
    }

    @GetMapping("/projects")
    public ResponseEntity findParticipatedProjects(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<FindProjectResponseDto> response = projectQueryRepository.findProjects(userPrincipal.getId());
        return new ResponseEntity(response, OK);
    }
}
