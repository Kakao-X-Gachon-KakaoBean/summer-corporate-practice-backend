package com.kakaobean.core.unit.application.issue;

import com.kakaobean.core.factory.issue.IssueFactory;
import com.kakaobean.core.factory.issue.dto.ModifyIssueRequestDtoFactory;
import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.issue.application.IssueService;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.IssueValidator;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.exception.IssueAccessException;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.unit.UnitTest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IssueServiceTest extends UnitTest {

    private IssueService issueService;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach(){
        issueService = new IssueService(
                issueRepository,
                commentRepository,
                new IssueValidator(projectMemberRepository)
        );
    }

    @Test
    void 로그인한_유저가_이슈를_등록한다() {
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(),Mockito.anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createMember()));

        //when
        issueService.registerIssue(new RegisterIssueRequestDto(1L, "이슈 제목", "이슈 설명", 2L));

        //then
        verify(issueRepository, times(1)).save(Mockito.any(Issue.class));
    }

    @Test
    void 이슈_생성자가_자신의_이슈를_삭제한다(){
        //given
        Issue issue = IssueFactory.createIssue(1L);
        given(issueRepository.findById(issue.getId())).willReturn(Optional.of(issue));
        given(projectMemberRepository.findByMemberIdAndProjectId(3L, 1L))
                .willReturn(Optional.of(ProjectMemberFactory.createMember()));


        //when
        issueService.removeIssue(issue.getWriterId(), issue.getId());

        //then
        verify(issueRepository, times(1)).delete(Mockito.any(Issue.class));
        verify(commentRepository, times(1)).deleteByIssueId(issue.getId());

    }

    @Test
    void 이슈_생성자가_아닌_사람이_이슈를_삭제할_수_없다(){
        //given
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(ProjectMemberFactory.createMember()));
        given(issueRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(IssueFactory.createIssue(1L)));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            issueService.removeIssue(5L, 1L);
        });

        // then
        result.isInstanceOf(IssueAccessException.class);
    }

    @Test
    void 작성자가_이슈를_수정한다() {
        // given
        Issue issue = IssueFactory.createIssue(1L);
        given(issueRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(issue));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createMember()));

        // when
        issueService.modifyIssue(ModifyIssueRequestDtoFactory.createWithId(issue.getWriterId(), 1L));

        // then
        assertThat(issue.getTitle()).isEqualTo("수정된 이슈 제목");
        assertThat(issue.getContent()).isEqualTo("수정된 이슈 내용");
    }

    @Test
    void 작성자가_아니면_이슈를_수정할_수_없다() {
        // given
        Issue issue = IssueFactory.createIssue(1L);
        given(issueRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(issue));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(createMember()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            issueService.modifyIssue(ModifyIssueRequestDtoFactory.createWithId(2L, 1L));
        });

        // then
        result.isInstanceOf(IssueAccessException.class);
    }
}
