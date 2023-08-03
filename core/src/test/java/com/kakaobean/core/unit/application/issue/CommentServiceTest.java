package com.kakaobean.core.unit.application.issue;

import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.unit.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.issue.IssueFactory.createIssue;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createMember;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommentServiceTest extends UnitTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @BeforeEach
    void beforeEach(){
        commentService = new CommentService(
                commentRepository,
                issueRepository
        );
    }


    @Test
    void 댓글_작성(){
        //given
        given(issueRepository.findByIssueId(Mockito.anyLong())).willReturn(Optional.ofNullable(createIssue(3L)));

        //when
        commentService.registerComment(new RegisterCommentRequestDto(1L, 2L,"댓글 내용"));

        //then
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
    }

}
