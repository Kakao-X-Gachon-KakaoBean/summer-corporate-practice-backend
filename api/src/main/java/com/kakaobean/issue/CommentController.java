package com.kakaobean.issue;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity registerComment(@Validated @RequestBody RegisterCommentRequest request,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        commentService.registerComment(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("댓글이 생성되었습니다."), CREATED);
    }
//    TODO:
//     1. 댓글 생성
//     2. 댓글 삭제
//     3. 댓글 전체 조회
}
