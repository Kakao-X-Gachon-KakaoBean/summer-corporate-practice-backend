package com.kakaobean.sprint;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.core.sprint.domain.repository.query.FindAllSprintResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.FindSprintResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.SprintQueryRepository;
import com.kakaobean.security.UserPrincipal;
import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Timed("api.sprint")
@RestController
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;
    private final SprintQueryRepository sprintQueryRepository;

    @PostMapping("/sprints")
    public ResponseEntity registerSprint(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @Validated @RequestBody RegisterSprintRequest request) {
        log.info("스프린트 등록 api 시작");
        Long sprintId = sprintService.registerSprint(request.toServiceDto(userPrincipal.getId()));
        log.info("스프린트 등록 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from(sprintId, "스프린트가 생성되었습니다."), CREATED);
    }

    @PatchMapping("/sprints/{sprintId}")
    public ResponseEntity modifySprint(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long sprintId,
                                       @Validated @RequestBody ModifySprintRequest request) {
        log.info("스프린트 수정 api 시작");
        sprintService.modifySprint(request.toServiceDto(userPrincipal.getId(), sprintId));
        log.info("스프린트 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("스프린트 정보가 수정되었습니다."), OK);
    }

    @DeleteMapping("/sprints/{sprintId}")
    public ResponseEntity removeSprint(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long sprintId) {
        log.info("스프린트 제거 api 시작");
        sprintService.removeSprint(userPrincipal.getId(), sprintId);
        log.info("스프린트 제거 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("스프린트가 삭제되었습니다."), OK);
    }

    @GetMapping("/sprints")
    public ResponseEntity findAllSprints(@RequestParam Long projectId) {
        log.info("스프린트 전체 조회 api 시작");
        FindAllSprintResponseDto response = sprintQueryRepository.findAllByProjectId(projectId);
        log.info("스프린트 전체 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/sprints/{sprintId}")
    public ResponseEntity findSprint(@PathVariable Long sprintId) {
        log.info("스프린트 단일 조회 api 시작");
        FindSprintResponseDto response = sprintQueryRepository.findSprintById(sprintId);
        log.info("스프린트 단일 조회 api 종료");
        return new ResponseEntity(response, OK);
    }
}
