package com.kakaobean.sprint;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.security.UserPrincipal;
import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @PostMapping("/sprints")
    public ResponseEntity registerSprint(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @Validated @RequestBody RegisterSprintRequest request) {
        sprintService.registerSprint(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("스프린트가 생성되었습니다."), CREATED);
    }

    @PatchMapping("/sprints/{sprintId}")
    public ResponseEntity modifySprint(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long sprintId,
                                       @Validated @RequestBody ModifySprintRequest request) {
        sprintService.modifySprint(request.toServiceDto(userPrincipal.getId(), sprintId));
        return new ResponseEntity(CommandSuccessResponse.from("스프린트 정보가 수정되었습니다."), OK);
    }

    @DeleteMapping("/sprints/{sprintId}")
    public ResponseEntity removeSprint(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long sprintId) {
        sprintService.removeSprint(userPrincipal.getId(), sprintId);
        return new ResponseEntity(CommandSuccessResponse.from("스프린트가 삭제되었습니다."), OK);
    }}
