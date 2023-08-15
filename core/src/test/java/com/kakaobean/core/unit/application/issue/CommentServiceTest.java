package com.kakaobean.core.unit.application.issue;

import com.kakaobean.core.factory.issue.CommentFactory;
import com.kakaobean.core.factory.issue.IssueFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.CommentValidator;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.CommentAccessException;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.issue.IssueFactory.createIssue;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommentServiceTest extends UnitTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach(){
        commentService = new CommentService(
                commentRepository,
                issueRepository,
                new CommentValidator(projectMemberRepository)
        );
    }


    @Test
    void 댓글_작성(){
        //given
        given(issueRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(createIssue(3L)));

        //when
        commentService.registerComment(new RegisterCommentRequestDto(1L, 2L,"댓글 내용"));

        //then
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void 댓글_삭제(){
        //given
        Comment comment = CommentFactory.create();
        given(commentRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(comment));

        //when
        commentService.removeComment(comment.getWriterId(), 2L);

        //then
        verify(commentRepository, times(1)).delete(Mockito.any(Comment.class));
    }

    @Test
    void 자신이_작성하지_않은_댓글은_삭제할_수_없다(){
        //given
        Comment comment = CommentFactory.createCommentWithMemberIdAndIssueId(2L, Mockito.anyLong());
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            commentService.removeComment(3L, comment.getIssueId());
        });

        //then
        result.isInstanceOf(CommentAccessException.class);
    }
}
