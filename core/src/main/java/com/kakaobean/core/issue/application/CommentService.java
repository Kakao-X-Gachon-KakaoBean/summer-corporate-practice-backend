package com.kakaobean.core.issue.application;

import com.kakaobean.core.issue.application.dto.request.ModifyCommentRequestDto;
import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.CommentValidator;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.NotExistsIssueException;
import com.kakaobean.core.issue.exception.NotExistsCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final IssueRepository issueRepository;

    private final CommentValidator commentValidator;

    @Transactional(readOnly = false)
    public void registerComment(RegisterCommentRequestDto dto){
        Comment comment = dto.toEntity();
        issueRepository.findById(dto.getIssueId()).orElseThrow(NotExistsIssueException::new);
        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(Long memberId, Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotExistsCommentException::new);

        commentValidator.validateAccess(comment, memberId);

        commentRepository.delete(comment);
    }

//    @Transactional
//    public void modifyComment(ModifyCommentRequestDto dto) {
//        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(NotExistsCommentException::new);
//        commentValidator.validateAccess(comment, dto.getWriterId());
//        comment.modify(dto.getContent());
//    }
}
