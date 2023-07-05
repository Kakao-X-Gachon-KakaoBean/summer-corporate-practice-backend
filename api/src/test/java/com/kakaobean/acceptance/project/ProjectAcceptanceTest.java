package com.kakaobean.acceptance.project;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.TestMember;
import com.kakaobean.acceptance.auth.AuthAcceptanceTask;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.*;
import static org.assertj.core.api.Assertions.*;

public class ProjectAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;


    @Test
    void 프로젝트를_만든다(){

        //then
        RegisterProjectRequest request = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");

        ExtractableResponse response = ProjectAcceptanceTask.registerProjectTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void 프로젝트에_멤버를_초대한다(){

        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);
        InviteProjectMemberRequest request = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));

        //when
        ExtractableResponse response = ProjectAcceptanceTask.inviteProjectMemberTask(request, project.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 초대받은_멤버가_프로젝트에_가입한다(){
        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        RegisterProjectMemberRequest request = new RegisterProjectMemberRequest(project.getSecretKey());

        //when
        ExtractableResponse response = ProjectAcceptanceTask.joinProjectMemberTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }
}

/**
 * 클래스에 @BeforeEach가 있으면
 * 부모 클래스에 @BeforeEach가 실행되지 않음.
 */