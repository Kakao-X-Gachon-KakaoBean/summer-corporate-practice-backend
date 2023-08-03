package com.kakaobean.acceptance.sprint;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.query.FindAllSprintResponseDto;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.unit.controller.factory.sprint.ModifySprintRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterTaskRequestFactory;
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
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());

        //when
        ExtractableResponse response = SprintAcceptanceTask.registerSprintTask(sprintRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(sprintRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 스프린트_수정(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        SprintAcceptanceTask.registerSprintTask(sprintRequest);
        Sprint sprint = sprintRepository.findAll().get(0);

        //스프린트 수정
        ModifySprintRequest request = ModifySprintRequestFactory.createRequest();

        //when
        ExtractableResponse response = SprintAcceptanceTask.modifySprintTask(request, sprint.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(sprintRepository.findById(sprint.getId()).get().getTitle()).isEqualTo("수정된 스프린트 제목");
    }

    @Test
    void 스프린트_삭제(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        SprintAcceptanceTask.registerSprintTask(sprintRequest);
        Sprint sprint = sprintRepository.findAll().get(0);

        //스프린트 삭제
        //when
        ExtractableResponse response = SprintAcceptanceTask.removeSprintTask(sprint.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(sprintRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void 스프린트_전체_조회(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        SprintAcceptanceTask.registerSprintTask(sprintRequest);
        Sprint sprint = sprintRepository.findAll().get(0);

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprint.getId());
        TaskAcceptanceTask.registerTaskTask(taskRequest);

        //스프린트 전체 조회
        //when
        ExtractableResponse response = SprintAcceptanceTask.findAllSprintsTask(project.getId());

        //then
        FindAllSprintResponseDto dto = response.as(FindAllSprintResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(dto.getSprints().size()).isEqualTo(1);
    }

    @Test
    void 스프린트_조회(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(projectRequest);
        Project project = projectRepository.findAll().get(0);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        SprintAcceptanceTask.registerSprintTask(sprintRequest);
        Sprint sprint = sprintRepository.findAll().get(0);

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprint.getId());
        TaskAcceptanceTask.registerTaskTask(taskRequest);

        //스프린트 조회
        //when
        ExtractableResponse response = SprintAcceptanceTask.findSprintTask(sprint.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
