package com.kakaobean.acceptance.sprint;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SprintAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SprintRepository sprintRepository;

    @Test
    void 스프린트_생성(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.create();

        //when
        ExtractableResponse response = SprintAcceptanceTask.registerSprintTask(sprintRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(sprintRepository.findAll().size()).isEqualTo(1);
    }
}
