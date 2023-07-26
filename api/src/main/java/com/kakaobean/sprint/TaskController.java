package com.kakaobean.sprint;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.security.UserPrincipal;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity registerTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @Validated @RequestBody RegisterTaskRequest request){
        taskService.registerTask(request.toService(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("테스크 새성되었습니다."), CREATED);
    }

}
