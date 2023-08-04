package com.kakaobean.core.issue.application;

import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.NotExistsIssueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final IssueRepository issueRepository;

    @Transactional(readOnly = false)
    public void registerComment(RegisterCommentRequestDto dto){
        Comment comment = dto.toEntity();
        issueRepository.findById(dto.getIssueId()).orElseThrow(NotExistsIssueException::new);
        commentRepository.save(comment);
    }
}
