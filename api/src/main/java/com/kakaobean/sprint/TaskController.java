package com.kakaobean.sprint;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.security.UserPrincipal;
import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity registerTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @Validated @RequestBody RegisterTaskRequest request){
        taskService.registerTask(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("테스크가 생성되었습니다."), CREATED);
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity modifyTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @PathVariable Long taskId,
                                     @Validated @RequestBody ModifyTaskRequest request){
        taskService.modifyTask(request.toServiceDto(userPrincipal.getId(), taskId));
        return new ResponseEntity(CommandSuccessResponse.from("테스크가 수정되었습니다."), OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity modifyTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @PathVariable Long taskId){
        taskService.removeTask(userPrincipal.getId(), taskId);
        return new ResponseEntity(CommandSuccessResponse.from("테스크가 삭제되었습니다."), OK);
    }

    @PatchMapping("/tasks/{taskId}/assignment/{memberId}")
    public ResponseEntity assignTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @PathVariable Long taskId,
                                     @PathVariable Long memberId){
        taskService.assignTask(userPrincipal.getId(), taskId, memberId);
        return new ResponseEntity(CommandSuccessResponse.from("작업이 할당되었습니다."), OK);
    }

}
