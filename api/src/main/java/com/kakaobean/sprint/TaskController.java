package com.kakaobean.sprint;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.repository.query.FindSprintResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.FindTaskResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.TaskQueryRepository;
import com.kakaobean.security.UserPrincipal;
import com.kakaobean.sprint.dto.request.ChangeWorkStatusRequest;
import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
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
@Timed("api.spring.task")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskQueryRepository taskQueryRepository;

    @PostMapping("/tasks")
    public ResponseEntity registerTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @Validated @RequestBody RegisterTaskRequest request) {
        log.info("테스크 생성 api 시작");
        taskService.registerTask(request.toServiceDto(userPrincipal.getId()));
        log.info("테스크 생성 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("테스크가 생성되었습니다."), CREATED);
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity modifyTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @PathVariable Long taskId,
                                     @Validated @RequestBody ModifyTaskRequest request) {
        log.info("테스크 수정 api 시작");
        taskService.modifyTask(request.toServiceDto(userPrincipal.getId(), taskId));
        log.info("테스크 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("테스크 정보가 수정되었습니다."), OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity removeTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @PathVariable Long taskId) {
        log.info("테스크 삭제 api 시작");
        taskService.removeTask(userPrincipal.getId(), taskId);
        log.info("테스크 삭제 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("테스크가 삭제되었습니다."), OK);
    }

    @PatchMapping("/tasks/{taskId}/assignment/{memberId}")
    public ResponseEntity assignTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @PathVariable Long taskId,
                                     @PathVariable Long memberId) {
        log.info("테스크 할당 api 시작");
        taskService.assignTask(userPrincipal.getId(), taskId, memberId);
        log.info("테스크 할당 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("작업이 할당되었습니다."), OK);
    }

    @PatchMapping("/tasks/{taskId}/work-status")
    public ResponseEntity changeStatus(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long taskId,
                                       @RequestBody ChangeWorkStatusRequest request) {
        log.info("테스크 상태 수정 api 시작");
        taskService.changeStatus(request.toServiceDto(userPrincipal.getId(), taskId));
        log.info("테스크 상태 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("작업 상태가 변경되었습니다."), OK);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity findTask(@PathVariable Long taskId) {
        log.info("테스크 조회 api 시작");
        FindTaskResponseDto response = taskQueryRepository.findTask(taskId);
        log.info("테스크 조회 api 종료");
        return new ResponseEntity(response, OK);
    }
}
