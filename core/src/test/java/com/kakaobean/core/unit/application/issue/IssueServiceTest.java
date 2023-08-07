package com.kakaobean.core.unit.application.issue;

import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.unit.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IssueServiceTest extends UnitTest {

    private IssueService issueService;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach(){
        issueService = new IssueService(
                issueRepository,
                projectMemberRepository
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
}
