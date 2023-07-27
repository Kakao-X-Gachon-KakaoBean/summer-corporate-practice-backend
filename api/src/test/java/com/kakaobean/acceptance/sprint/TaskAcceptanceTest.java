package com.kakaobean.acceptance.sprint;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.unit.controller.factory.sprint.ModifyTaskRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterTaskRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void 테크스_생성(){

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

        //when
        ExtractableResponse response = TaskAcceptanceTask.registerTaskTask(taskRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(taskRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 테스크_수정(){

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
        Task task = taskRepository.findAll().get(0);

        //테스크 수정
        ModifyTaskRequest modifyRequest = ModifyTaskRequestFactory.createRequestWithId(sprint.getId());

        //when
        ExtractableResponse response = TaskAcceptanceTask.modifyTaskTask(modifyRequest, task.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(taskRepository.findById(task.getId()).get().getTitle()).isEqualTo("수정된 테스크 제목");
    }

    @Test
    void 테스크_삭제(){

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
        Task task = taskRepository.findAll().get(0);

        //테스크 삭제
        //when
        ExtractableResponse response = TaskAcceptanceTask.removeTaskTask(task.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(taskRepository.findAll().size()).isEqualTo(0);
    }
}
