package com.kakaobean.acceptance.project;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.auth.AuthAcceptanceTask;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.core.project.domain.Project;
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

import static org.assertj.core.api.Assertions.*;

public class ProjectAcceptanceTest extends AcceptanceTest {


    RegisterProjectRequest registerProjectRequest;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void beforeEach(){
         registerProjectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
    }


    @Test
    void 프로젝트를_만든다(){

        //then
        ExtractableResponse response = ProjectAcceptanceTask.registerProjectTask(registerProjectRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }

//    @Test
//    void 프로젝트에_초대한다(){
//        //given
//        ProjectAcceptanceTask.registerProjectTask(registerProjectRequest);
//        Project project = projectRepository.findAll().get(0);
//        //RegisterProjectMemberRequest request = new InviteProjectMemberRequest();
//
//        //when
//        //ExtractableResponse response = ProjectAcceptanceTask.inviteProjectMemberTask();
//
//        //then
//        //assertThat(response.statusCode()).isEqualTo(201);
//    }
}
