package com.kakaobean.acceptance.sprint;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.query.FindAllSprintResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.FindSprintResponseDto;
import com.kakaobean.core.sprint.exception.NotExistsSprintException;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.fixture.sprint.ModifySprintRequestFactory;
import com.kakaobean.fixture.sprint.RegisterSprintRequestFactory;
import com.kakaobean.fixture.sprint.RegisterTaskRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SprintAcceptanceTest extends AcceptanceTest {


    @Test
    void 스프린트_생성(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());

        //when
        ExtractableResponse response = SprintAcceptanceTask.registerSprintTask(sprintRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);

        Long sprintId = response.as(CommandSuccessResponse.Created.class).getId();
        FindSprintResponseDto result = SprintAcceptanceTask.findSprintTask(sprintId).as(FindSprintResponseDto.class);
        assertThat(result).isNotNull();
    }

    @Test
    void 스프린트_수정(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);


        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //스프린트 수정
        ModifySprintRequest request = ModifySprintRequestFactory.createRequest();

        //when
        ExtractableResponse response = SprintAcceptanceTask.modifySprintTask(request, sprintId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindSprintResponseDto result = SprintAcceptanceTask.findSprintTask(sprintId).as(FindSprintResponseDto.class);
        assertThat(result.getSprintTitle()).isEqualTo("수정된 스프린트 제목");
    }

    @Test
    void 스프린트_삭제(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //스프린트 삭제
        //when
        ExtractableResponse response = SprintAcceptanceTask.removeSprintTask(sprintId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        assertThat(sprintRepository.findById(sprintId).isEmpty()).isTrue();

        /** 아래와 같은 코드로도 검증 불가능.
         * assertThatThrownBy(() -> {
         *             SprintAcceptanceTask.findSprintTask(sprintId);
         *         }).isInstanceOf(NotExistsSprintException.class);
         */
    }

    @Test
    void 스프린트_전체_조회(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);


        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintId);
        TaskAcceptanceTask.registerTaskTask(taskRequest);

        //스프린트 전체 조회
        //when
        ExtractableResponse response = SprintAcceptanceTask.findAllSprintsTask(projectResponse.getId());

        //then
        FindAllSprintResponseDto dto = response.as(FindAllSprintResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(dto.getSprints().size()).isEqualTo(1);
    }

    @Test
    void 스프린트_조회(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintId);
        TaskAcceptanceTask.registerTaskTask(taskRequest);

        //스프린트 조회
        //when
        ExtractableResponse response = SprintAcceptanceTask.findSprintTask(sprintId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
