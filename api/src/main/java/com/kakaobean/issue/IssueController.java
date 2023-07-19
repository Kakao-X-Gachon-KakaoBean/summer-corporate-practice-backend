package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.member.dto.ModifyMemberRequest;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/projects/{projectId}/issue")
    public ResponseEntity<RegisterIssueResponseDto> registerIssue(@Validated @RequestBody RegisterIssueRequest request,
                                                                  @AuthenticationPrincipal UserPrincipal userPrincipal){
        RegisterIssueResponseDto res = issueService.registerIssue(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(res, CREATED);
    }
}
