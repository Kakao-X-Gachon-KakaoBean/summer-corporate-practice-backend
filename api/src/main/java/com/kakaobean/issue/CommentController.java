package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.issue.dto.ModifyCommentRequest;
import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Timed("api.issue.comment")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity registerComment(@Validated @RequestBody RegisterCommentRequest request,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        log.info("이슈 댓글 생성 요청 api 시작");
        commentService.registerComment(request.toServiceDto(userPrincipal.getId()));
        log.info("이슈 댓글 생성 요청 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("댓글이 생성되었습니다."), CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity removeComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable Long commentId) {
        log.info("이슈 댓글 삭제 요청 api 시작");
        commentService.removeComment(userPrincipal.getId(), commentId);
        log.info("이슈 댓글 삭제 요청 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("댓글이 삭제되었습니다."), OK);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity modifyComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable Long commentId,
                                      @Validated @RequestBody ModifyCommentRequest request) {
        log.info("이슈 댓글 수정 요청 api 시작");
        commentService.modifyComment(request.toServiceDto(userPrincipal.getId(), commentId));
        log.info("이슈 댓글 수정 요청 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("댓글이 수정되었습니다."), OK);
    }
}
