package com.kakaobean.core.integration.issue;


import com.kakaobean.core.factory.issue.CommentFactory;
import com.kakaobean.core.factory.issue.IssueFactory;
import com.kakaobean.core.factory.issue.dto.ModifyIssueRequestDtoFactory;
import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.issue.application.CommentService;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.IssueAccessException;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.factory.issue.dto.RegisterIssueRequestDtoFactory.createWithId;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createWithMemberIdAndProjectId;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class IssueServiceTest extends IntegrationTest {

    @Autowired
    IssueService issueService;

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


    @Test
    void 일반_멤버가_이슈_생성에_성공한다() {

        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));

        // when
        Long issue = issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));

        // then
        assertThat(issueRepository.findById(issue).isPresent()).isTrue();
    }

    @Test
    void 게스트_멤버가_이슈_생성에_성공한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), VIEWER));

        // when
        Long issue = issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));

        // then
        assertThat(issueRepository.findById(issue).isPresent()).isTrue();
    }

    @Test
    void 관리자가_이슈_생성에_성공한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));

        // when
        Long issue = issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));

        // then
        assertThat(issueRepository.findById(issue).isPresent()).isTrue();
    }

    @Test
    void 프로젝트에_소속되지_않은_사용자이기에_이슈_생성에_실패한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), 9999999L, VIEWER));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));
        });

        // then
        result.isInstanceOf(NotExistsProjectMemberException.class);
    }

    @Test
    void  작성자가_이슈를_수정한다() {

        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), ADMIN));
        Issue issue = issueRepository.save(IssueFactory.createIssueWithMemberIdAndProjectId(projectMember.getMemberId(), project.getId()));

        // when
        issueService.modifyIssue(ModifyIssueRequestDtoFactory.createWithId(projectMember.getMemberId(), issue.getId()));

        // then
        assertThat(issueRepository.findById(issue.getId()).get().getTitle()).isEqualTo("수정된 이슈 제목");
        assertThat(issueRepository.findById(issue.getId()).get().getContent()).isEqualTo("수정된 이슈 내용");

    }

    @Test
    void 작성자가_아닌_멤버는_이슈를_수정할_수_없다() {
        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));
        Issue issue = issueRepository.save(IssueFactory.createIssue(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            issueService.modifyIssue(ModifyIssueRequestDtoFactory.createWithId(projectMember.getMemberId(), issue.getId()));
        });

        // then
        result.isInstanceOf(IssueAccessException.class);
    }

    @Test
    void 이슈_작성자가_이슈_삭제에_성공한다(){

        //given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));

        Issue issue = IssueFactory.createIssueWithMemberIdAndProjectId(projectMember.getMemberId(), project.getId());
        Issue savedIssue = issueRepository.save(issue);

        Comment comment = CommentFactory.createCommentWithMemberIdAndIssueId(projectMember.getMemberId(), savedIssue.getId());
        Comment savedComment = commentRepository.save(comment);

        //when
        issueService.removeIssue(projectMember.getMemberId(), savedIssue.getId());

        //then
        assertThat(issueRepository.findById(savedIssue.getId()).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedComment.getId()).isEmpty()).isTrue();
    }

    @Test
    void 작성자가_아니면_이슈를_삭제할_수_없다() {

        // given
        Project project = projectRepository.save(ProjectFactory.createWithoutId());
        ProjectMember issueWriter = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(MemberFactory.getMemberId(), project.getId(), MEMBER));
        Issue issue = issueRepository.save(IssueFactory.createIssueWithMemberIdAndProjectId(issueWriter.getMemberId(), project.getId()));
        commentRepository.save(CommentFactory.createCommentWithMemberIdAndIssueId(MemberFactory.getMemberId(), issue.getId()));
        commentRepository.save(CommentFactory.createCommentWithMemberIdAndIssueId(MemberFactory.getMemberId(), issue.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            issueService.removeIssue(projectMember.getMemberId(), issue.getId());
        });

        // then
        result.isInstanceOf(IssueAccessException.class);
    }
}
