package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.domain.repository.query.FindIndividualIssueResponseDto;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.issue.domain.repository.query.IssueQueryRepository;
import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Timed("api.issue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final IssueQueryRepository issueQueryRepository;

    @PostMapping("/issues")
    public ResponseEntity registerIssue(@Validated @RequestBody RegisterIssueRequest request,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("이슈 생성 요청 api 시작");
        Long issueId = issueService.registerIssue(request.toServiceDto(userPrincipal.getId()));
        log.info("이슈 생성 요청 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from(issueId, "이슈가 생성되었습니다."), CREATED);
    }

//    @Cacheable(cacheNames = "pagedIssues", key = "{#projectId, #page}")
    @GetMapping("/issues/page")
    public ResponseEntity findAllIssues(@RequestParam Long projectId, @RequestParam Integer page) {
        log.info("이슈 페이징 조회 api 시작");
        FindIssuesWithinPageResponseDto responseDto = issueQueryRepository.findByProjectId(projectId, page);
        log.info("이슈 페이징 조회 api 종료");
        return new ResponseEntity(responseDto, OK);
    }

    @GetMapping("/issues/{issueId}")
    public ResponseEntity findIssue(@PathVariable Long issueId) {
        log.info("단일 이슈 조회 api 시작");
        FindIndividualIssueResponseDto responseDto = issueQueryRepository.findByIssueId(issueId);
        log.info("단일 이슈 조회 api 종료");
        return new ResponseEntity(responseDto, OK);
    }

    @DeleteMapping("/issues/{issueId}")
    public ResponseEntity removeIssue(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long issueId) {
        log.info("이슈 제거 api 시작");
        issueService.removeIssue(userPrincipal.getId(), issueId);
        log.info("이슈 제거 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("이슈가 삭제되었습니다."), OK);
    }

    @PatchMapping("/issues/{issueId}")
    public ResponseEntity modifyIssue(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long issueId,
                                       @Validated @RequestBody ModifyIssueRequest request) {
        log.info("이슈 수정 api 시작");
        issueService.modifyIssue(request.toServiceDto(userPrincipal.getId(), issueId));
        log.info("이슈 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("이슈 정보가 수정되었습니다."), OK);
    }

}
