package com.kakaobean.core.integration.issue;

import com.kakaobean.core.factory.issue.CommentFactory;
import com.kakaobean.core.factory.issue.dto.ModifyCommentRequestDtoFactory;
import com.kakaobean.core.factory.issue.dto.RegisterCommentRequestDtoFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.sprint.dto.RegisterTaskRequestDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.request.ModifyCommentRequestDto;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.CommentAccessException;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.factory.issue.IssueFactory.createIssue;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createWithMemberIdAndProjectId;
import static com.kakaobean.core.factory.sprint.SprintFactory.createWithId;
import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;
import static com.kakaobean.core.project.domain.ProjectRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentServiceTest extends IntegrationTest {
    @Autowired
    CommentService commentService;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach() {
        issueRepository.deleteAll();
        commentRepository.deleteAll();
        projectRepository.deleteAll();
        projectMemberRepository.deleteAll();
    }

    @Test
    void 댓글을_쟉성한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Issue issue = issueRepository.save(createIssue(project.getId()));

        // when
        commentService.registerComment(RegisterCommentRequestDtoFactory.createWithId(issue.getId(), projectMember.getMemberId()));

        // then
        assertThat(commentRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 댓글을_수정한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Issue issue = issueRepository.save(createIssue(project.getId()));

        Comment comment = commentRepository.save(CommentFactory.createCommentWithMemberIdAndIssueId(projectMember.getMemberId(), issue.getId()));

        // when
        ModifyCommentRequestDto requestDto = ModifyCommentRequestDtoFactory.createWithIdAndMemberId(comment.getId(), projectMember.getMemberId());
        commentService.modifyComment(requestDto);

        Comment result = commentRepository.findById(comment.getId()).get();

        // then
        assertThat(result.getContent()).isEqualTo("수정된 댓글 내용");
    }

    @Test
    void 작성자가_아닌_유저는_댓글을_수정할_수_없다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Issue issue = issueRepository.save(createIssue(project.getId()));

        Comment comment = commentRepository.save(CommentFactory.createCommentWithMemberIdAndIssueId(projectMember.getMemberId(), issue.getId()));

        // when
        ModifyCommentRequestDto requestDto = ModifyCommentRequestDtoFactory.createWithIdAndMemberId(comment.getId(), 5L);

        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            commentService.modifyComment(requestDto);
        });

        // then
        result.isInstanceOf(CommentAccessException.class);
    }

    @Test
    void 댓글을_삭제한다(){
        //given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));

        Issue issue = issueRepository.save(createIssue(project.getId()));

        Comment comment = commentRepository.save(CommentFactory.createCommentWithMemberIdAndIssueId(projectMember.getMemberId(), issue.getId()));

        //when
        commentService.removeComment(projectMember.getMemberId(), comment.getId());

        //then
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void 댓글_작성자만이_댓글을_삭제할_수_있다(){
        //given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));

        Issue issue = issueRepository.save(createIssue(project.getId()));

        Comment comment = commentRepository.save(CommentFactory.createCommentWithMemberIdAndIssueId(projectMember.getMemberId(), issue.getId()));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(()->{
            commentService.removeComment(3L, comment.getId());
        });

        //then
        result.isInstanceOf(CommentAccessException.class);
    }
}
