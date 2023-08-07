package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.issue.domain.repository.query.IssueQueryRepository;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.security.UserPrincipal;
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

    private final IssueQueryRepository issueRepository;

    @PostMapping("/issues")
    public ResponseEntity registerIssue(@Validated @RequestBody RegisterIssueRequest request,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){
        issueService.registerIssue(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("이슈가 생성되었습니다."), CREATED);
    }

    @GetMapping("/issues/page")
    public ResponseEntity findAllIssue(@RequestParam Long projectId, @RequestParam Integer page){
        FindIssuesWithinPageResponseDto responseDto = issueRepository.findByProjectId(projectId, page);
        return new ResponseEntity(responseDto, OK);
    }

//    TODO:
//     1. 이슈 생성
//     2. 이슈 삭제
//     3. 이슈 전체 조회
//     4. 개별 이슈 조회
}
