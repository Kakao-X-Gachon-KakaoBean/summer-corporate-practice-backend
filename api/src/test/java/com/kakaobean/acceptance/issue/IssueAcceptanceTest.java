package com.kakaobean.acceptance.issue;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.acceptance.releasenote.ReleaseNoteAcceptanceTask;
import com.kakaobean.acceptance.sprint.SprintAcceptanceTask;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.issue.domain.repository.query.IssueQueryRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingReleaseNotesResponseDto;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.unit.controller.factory.issue.RegisterIssueRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.common.domain.BaseStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class IssueAcceptanceTest extends AcceptanceTest {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    IssueRepository issueRepository;

    @Test
    void 이슈_생성(){

        //given
        ////프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        ////이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.create();

        //when
        ExtractableResponse response = IssueAcceptanceTask.registerIssueTask(issueRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(issueRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 이슈를_8개씩_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);


        //이슈 생성
        for (int i = 0; i < 8; i++) {
            RegisterIssueRequest request = RegisterIssueRequestFactory.create();
            IssueAcceptanceTask.registerIssueTask(request);
        }

        //when
        ExtractableResponse response = IssueAcceptanceTask.findIssueTaskWithPaging(project.getId(), 0);

        //then
        FindIssuesWithinPageResponseDto result = response.as(FindIssuesWithinPageResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.isFinalPage()).isTrue();
        assertThat(result.getIssues().size()).isEqualTo(8);
    }

}
