package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.response.RegisterCommentResponseDto;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.member.dto.ModifyMemberRequest;
import com.kakaobean.member.dto.RegisterMemberRequest;
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

    @PostMapping("/projects/{projectId}/issues")
    public ResponseEntity<RegisterIssueResponseDto> registerIssue(@Validated @RequestBody RegisterIssueRequest request,
                                                                  @PathVariable Long projectId,
                                                                  @AuthenticationPrincipal UserPrincipal userPrincipal){
        RegisterIssueResponseDto res = issueService.registerIssue(request.toServiceDto(projectId, userPrincipal.getId()));
        return new ResponseEntity(res, CREATED);
    }

    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<RegisterCommentResponseDto> registerComment(@Validated @RequestBody RegisterCommentRequest request,
                                                                    @PathVariable Long issueId,
                                                                    @AuthenticationPrincipal UserPrincipal userPrincipal){
        RegisterCommentResponseDto res = issueService.registerComment(request.toServiceDto(issueId, userPrincipal.getId()));
        return new ResponseEntity(res, CREATED);
    }



//    TODO:
//     1. 이슈 생성
//     2. 이슈 삭제
//     3. 이슈 수정
//     4. 이슈 전체 조회
//     5. 개별 이슈 조회
}
