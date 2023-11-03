package com.kakaobean.acceptance.issue;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.issue.domain.Comment;
import com.kakaobean.core.issue.domain.Issue;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.issue.domain.repository.query.FindIndividualIssueResponseDto;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.fixture.member.MemberFactory;
import com.kakaobean.fixture.member.RegisterMemberRequestFactory;
import com.kakaobean.issue.dto.ModifyCommentRequest;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentAcceptanceTest extends AcceptanceTest {

    @Test
    void 댓글_생성(){

        ////프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        ////이슈 생성
        RegisterIssueRequest issueRequest = new RegisterIssueRequest("이슈 제목", "이슈 내용", projectResponse.getId());
        CommandSuccessResponse.Created issueResponse = IssueAcceptanceTask.registerIssueTask(issueRequest).as(CommandSuccessResponse.Created.class);

        ////코멘트 생성
        RegisterCommentRequest commentRequest = new RegisterCommentRequest("댓글 내용", issueResponse.getId());

        //when
        ExtractableResponse response = CommentAcceptanceTask.registerCommentTask(commentRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        
        FindIndividualIssueResponseDto result = IssueAcceptanceTask.findIssueWithId(issueResponse.getId()).as(FindIndividualIssueResponseDto.class);
        assertThat(result.getComments().size()).isEqualTo(1);
    }

    @Test
    void 댓글_삭제(){
        //given
        ////프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        System.out.println("memberContext.get().getAdmin().getId() = " + memberContext.get().getAdmin().getId());
        ////이슈 생성
        RegisterIssueRequest issueRequest = new RegisterIssueRequest("이슈 제목", "이슈 내용", projectResponse.getId());
        CommandSuccessResponse.Created issueResponse = IssueAcceptanceTask.registerIssueTask(issueRequest).as(CommandSuccessResponse.Created.class);
        
        ////코멘트 생성
        RegisterCommentRequest commentRequest = new RegisterCommentRequest("댓글 내용", issueResponse.getId());
        CommandSuccessResponse.Created commentResponse = CommentAcceptanceTask.registerCommentTask(commentRequest).as(CommandSuccessResponse.Created.class);

        //when
        ExtractableResponse response = CommentAcceptanceTask.deleteCommentTask(commentResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        FindIndividualIssueResponseDto result = IssueAcceptanceTask.findIssueWithId(issueResponse.getId()).as(FindIndividualIssueResponseDto.class);
        assertThat(result.getComments().size()).isEqualTo(0);
    }

    @Test
    void 댓글_수정(){
        //given
        ////프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        ////이슈 생성
        RegisterIssueRequest issueRequest = new RegisterIssueRequest("이슈 제목", "이슈 내용", projectResponse.getId());
        CommandSuccessResponse.Created issueResponse = IssueAcceptanceTask.registerIssueTask(issueRequest).as(CommandSuccessResponse.Created.class);

        ////코멘트 생성
        RegisterCommentRequest commentRequest = new RegisterCommentRequest("댓글 내용", issueResponse.getId());
        CommandSuccessResponse.Created commentResponse = CommentAcceptanceTask.registerCommentTask(commentRequest).as(CommandSuccessResponse.Created.class);

        ////댓글 수정
        ModifyCommentRequest modifyCommentRequest = new ModifyCommentRequest("수정된 댓글 내용");

        //when
        ExtractableResponse response = CommentAcceptanceTask.modifyCommentTask(modifyCommentRequest, commentResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        FindIndividualIssueResponseDto result = IssueAcceptanceTask.findIssueWithId(issueResponse.getId()).as(FindIndividualIssueResponseDto.class);
        assertThat(result.getComments().get(0).getContent()).isEqualTo("수정된 댓글 내용");
    }
}
