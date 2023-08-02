package com.kakaobean.issue;

import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.response.RegisterCommentResponseDto;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<RegisterCommentResponseDto> registerComment(@Validated @RequestBody RegisterCommentRequest request,
                                                                      @PathVariable Long issueId,
                                                                      @AuthenticationPrincipal UserPrincipal userPrincipal){
        RegisterCommentResponseDto res = commentService.registerComment(request.toServiceDto(issueId, userPrincipal.getId()));
        return new ResponseEntity(res, CREATED);
    }
}
