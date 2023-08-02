package com.kakaobean.acceptance.issue;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.unit.controller.factory.issue.RegisterCommentRequestFactory;
import com.kakaobean.unit.controller.factory.issue.RegisterIssueRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentAcceptanceTest extends AcceptanceTest {
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
        IssueAcceptanceTask.registerIssueTask(issueRequest, project.getId());
        Issue issue = issueRepository.findAll().get(0);

        ////코멘트 생성
        RegisterCommentRequest commentRequest = RegisterCommentRequestFactory.create();

        //when
        ExtractableResponse response = CommentAcceptanceTask.registerCommentTask(commentRequest, issue.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(commentRepository.findAll().size()).isEqualTo(1);
    }
}
