package com.kakaobean.core.issue.domain;

import com.kakaobean.core.issue.exception.CommentAccessException;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final ProjectMemberRepository projectMemberRepository;

    public void validateAccess(Comment comment, Long memberId) {
        if(memberId != comment.getWriterId()){
            throw new CommentAccessException();
        }
    }
}
