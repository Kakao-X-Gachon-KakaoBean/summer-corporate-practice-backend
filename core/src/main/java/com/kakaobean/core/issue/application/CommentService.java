package com.kakaobean.core.issue.application;

import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import com.kakaobean.core.issue.application.dto.response.RegisterCommentResponseDto;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = false)
    public RegisterCommentResponseDto registerComment(RegisterCommentRequestDto dto){
        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return new RegisterCommentResponseDto(comment.getCommentId());
    }
}
