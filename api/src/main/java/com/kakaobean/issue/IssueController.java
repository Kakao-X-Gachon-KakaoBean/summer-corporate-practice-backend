package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.domain.repository.query.FindIndividualIssueResponseDto;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.issue.domain.repository.query.IssueQueryRepository;
import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.security.UserPrincipal;
import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final IssueQueryRepository issueQueryRepository;

    @PostMapping("/issues")
    public ResponseEntity registerIssue(@Validated @RequestBody RegisterIssueRequest request,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){
        issueService.registerIssue(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("이슈가 생성되었습니다."), CREATED);
    }

    @GetMapping("/issues/page")
    public ResponseEntity findAllIssues(@RequestParam Long projectId, @RequestParam Integer page){
        FindIssuesWithinPageResponseDto responseDto = issueQueryRepository.findByProjectId(projectId, page);
        return new ResponseEntity(responseDto, OK);
    }

    @GetMapping("/issues/{issueId}")
    public ResponseEntity findIssue(@PathVariable Long issueId){
        FindIndividualIssueResponseDto responseDto = issueQueryRepository.findByIssueId(issueId);
        return new ResponseEntity(responseDto, OK);
    }

    @PatchMapping("/issues/{issueId}")
    public ResponseEntity modifyIssue(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long issueId,
                                       @Validated @RequestBody ModifyIssueRequest request) {
        issueService.modifyIssue(request.toServiceDto(userPrincipal.getId(), issueId));
        return new ResponseEntity(CommandSuccessResponse.from("이슈 정보가 수정되었습니다."), OK);
    }

}
