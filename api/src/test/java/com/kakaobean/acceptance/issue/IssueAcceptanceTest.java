package com.kakaobean.acceptance.issue;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.domain.repository.query.FindIndividualIssueResponseDto;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;

import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;

import com.kakaobean.fixture.issue.ModifyIssueRequestFactory;
import com.kakaobean.fixture.issue.RegisterCommentRequestFactory;
import com.kakaobean.fixture.issue.RegisterIssueRequestFactory;

import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueAcceptanceTest extends AcceptanceTest {


    @Test
    void 이슈_생성(){

        //given
        ////프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        ////이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(projectResponse.getId());

        //when
        ExtractableResponse response = IssueAcceptanceTask.registerIssueTask(issueRequest);

        //then
        CommandSuccessResponse.Created issueResponse = response.as(CommandSuccessResponse.Created.class);
        assertThat(response.statusCode()).isEqualTo(201);

        ExtractableResponse result = IssueAcceptanceTask.findIssueWithId(issueResponse.getId());
        assertThat(result.statusCode()).isEqualTo(200);
    }

    @Test
    void 이슈_수정(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(projectResponse.getId());
        CommandSuccessResponse.Created issueResponse = IssueAcceptanceTask.registerIssueTask(issueRequest).as(CommandSuccessResponse.Created.class);

        //이슈 수정
        ModifyIssueRequest request = ModifyIssueRequestFactory.createRequest();

        //when
        ExtractableResponse response = IssueAcceptanceTask.modifyIssueTask(request, issueResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindIndividualIssueResponseDto result = IssueAcceptanceTask.findIssueWithId(issueResponse.getId()).as(FindIndividualIssueResponseDto.class);
        assertThat(result.getIssue().getTitle()).isEqualTo("수정된 이슈 제목");
    }

    @Test
    void 이슈_삭제(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);


        //이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(projectResponse.getId());
        CommandSuccessResponse.Created issueResponse = IssueAcceptanceTask.registerIssueTask(issueRequest).as(CommandSuccessResponse.Created.class);


        //댓글 생성
        RegisterCommentRequest commentRequest = RegisterCommentRequestFactory.createWithIssueId(issueResponse.getId());
        CommentAcceptanceTask.registerCommentTask(commentRequest);

        //이슈 삭제
        //when
        ExtractableResponse response = IssueAcceptanceTask.removeIssueTask(issueResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindIndividualIssueResponseDto result = IssueAcceptanceTask.findIssueWithId(issueResponse.getId()).as(FindIndividualIssueResponseDto.class);
        assertThat(result.getComments().size()).isEqualTo(0);
        assertThat(result.getIssue()).isNull();
    }

    @Test
    void 이슈를_8개씩_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //이슈 생성
        for (int i = 0; i < 8; i++) {
            RegisterIssueRequest request = RegisterIssueRequestFactory.createWithProjectId(projectResponse.getId());
            IssueAcceptanceTask.registerIssueTask(request);
        }

        //when
        ExtractableResponse response = IssueAcceptanceTask.findIssueWithPagingTask(projectResponse.getId(), 0);

        //then
        FindIssuesWithinPageResponseDto result = response.as(FindIssuesWithinPageResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.isFinalPage()).isTrue();
        assertThat(result.getIssues().size()).isEqualTo(8);
    }

    @Test
    void 개별_이슈_조회(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(projectResponse.getId());
        CommandSuccessResponse.Created issueResponse = IssueAcceptanceTask.registerIssueTask(issueRequest).as(CommandSuccessResponse.Created.class);

        //댓글 생성
        RegisterCommentRequest commentRequest = RegisterCommentRequestFactory.createWithIssueId(issueResponse.getId());
        CommentAcceptanceTask.registerCommentTask(commentRequest);

        //이슈 조회
        //when
        ExtractableResponse response = IssueAcceptanceTask.findIssueWithId(issueResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindIndividualIssueResponseDto result = response.as(FindIndividualIssueResponseDto.class);
        assertThat(result.getIssue()).isNotNull();
        assertThat(result.getComments().size()).isEqualTo(1);
    }
}

