package com.kakaobean.acceptance.issue;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;

import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.domain.repository.query.FindIssuesWithinPageResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;

import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;

import com.kakaobean.unit.controller.factory.issue.ModifyIssueRequestFactory;
import com.kakaobean.unit.controller.factory.issue.RegisterCommentRequestFactory;
import com.kakaobean.unit.controller.factory.issue.RegisterIssueRequestFactory;

import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueAcceptanceTest extends AcceptanceTest {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    CommentRepository commentRepository;

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
    void 이슈_수정(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //스프린트 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(project.getId());
        IssueAcceptanceTask.registerIssueTask(issueRequest);
        Issue issue = issueRepository.findAll().get(0);

        //스프린트 수정
        ModifyIssueRequest request = ModifyIssueRequestFactory.createRequest();

        //when
        ExtractableResponse response = IssueAcceptanceTask.modifyIssueTask(request, issue.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(issueRepository.findById(issue.getId()).get().getTitle()).isEqualTo("수정된 이슈 제목");
    }

    @Test
    void 이슈_삭제(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(project.getId());
        IssueAcceptanceTask.registerIssueTask(issueRequest);
        Issue issue = issueRepository.findAll().get(0);

        //댓글 생성
        RegisterCommentRequest commentRequest = RegisterCommentRequestFactory.createWithIssueId(issue.getId());
        CommentAcceptanceTask.registerCommentTask(commentRequest);

        //이슈 삭제
        //when
        ExtractableResponse response = IssueAcceptanceTask.removeIssueTask(issue.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(issueRepository.findAll().size()).isEqualTo(0);
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void 이슈를_8개씩_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);


        //이슈 생성
        for (int i = 0; i < 8; i++) {
            RegisterIssueRequest request = RegisterIssueRequestFactory.createWithProjectId(project.getId());
            IssueAcceptanceTask.registerIssueTask(request);
        }

        //when
        ExtractableResponse response = IssueAcceptanceTask.findIssueWithPagingTask(project.getId(), 0);

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
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //이슈 생성
        RegisterIssueRequest issueRequest = RegisterIssueRequestFactory.createWithProjectId(project.getId());
        IssueAcceptanceTask.registerIssueTask(issueRequest);
        Issue issue = issueRepository.findAll().get(0);

        //댓글 생성
        RegisterCommentRequest commentRequest = RegisterCommentRequestFactory.createWithIssueId(issue.getId());
        CommentAcceptanceTask.registerCommentTask(commentRequest);

        //이슈 조회
        //when
        ExtractableResponse response = IssueAcceptanceTask.findIssueWithId(issue.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
