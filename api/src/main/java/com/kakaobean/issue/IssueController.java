package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/issues")
    public ResponseEntity registerIssue(@Validated @RequestBody RegisterIssueRequest request,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){
        issueService.registerIssue(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("이슈가 생성되었습니다."), CREATED);
    }
//    TODO:
//     1. 이슈 생성
//     2. 이슈 삭제
//     3. 이슈 수정
//     4. 이슈 전체 조회
//     5. 개별 이슈 조회
}
