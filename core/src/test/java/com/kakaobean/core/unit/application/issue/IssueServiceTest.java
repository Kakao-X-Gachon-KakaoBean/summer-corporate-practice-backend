package com.kakaobean.core.unit.application.issue;

import com.kakaobean.core.factory.project.ProjectMemberFactory;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.domain.ProjectValidator;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.unit.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.issue.IssueFactory.create;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IssueServiceTest extends UnitTest {

    private IssueService issueService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach(){
        issueService = new IssueService(
                issueRepository,
                commentRepository,
                projectMemberRepository
        );
    }

    @DisplayName("성공적으로 이슈를 등록한다.")
    @Test
    void successRegisterIssue() {
        //given
        given(issueRepository.save(Mockito.any(Issue.class))).willReturn(create(1L));
        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(),Mockito.anyLong())).willReturn(Optional.of(ProjectMemberFactory.createMember()));

        //when
        RegisterIssueResponseDto responseDto = issueService.registerIssue(new RegisterIssueRequestDto(1L, "이슈 제목", "이슈 설명", 2L));

        //then
        verify(issueRepository, times(1)).save(Mockito.any(Issue.class));
    }


}
