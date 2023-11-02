package com.kakaobean.acceptance.sprint;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.WorkStatus;
import com.kakaobean.core.sprint.domain.repository.query.FindTaskResponseDto;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.sprint.dto.request.ChangeWorkStatusRequest;
import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.fixture.member.RegisterMemberRequestFactory;
import com.kakaobean.fixture.sprint.ModifyTaskRequestFactory;
import com.kakaobean.fixture.sprint.RegisterSprintRequestFactory;
import com.kakaobean.fixture.sprint.RegisterTaskRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.QueueInformation;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.ADMIN;
import static com.kakaobean.acceptance.TestMember.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskAcceptanceTest extends AcceptanceTest {


    @Test
    void 테크스_생성(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintId);

        //when
        ExtractableResponse response = TaskAcceptanceTask.registerTaskTask(taskRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(201);

        Long taskId = response.as(CommandSuccessResponse.Created.class).getId();
        FindTaskResponseDto result = TaskAcceptanceTask.findTaskTask(taskId).as(FindTaskResponseDto.class);
        assertThat(result).isNotNull();
    }

    @Test
    void 테스크_수정(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintId);
        Long taskId = TaskAcceptanceTask.registerTaskTask(taskRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 수정
        ModifyTaskRequest modifyRequest = ModifyTaskRequestFactory.createRequestWithId(sprintId);

        //when
        ExtractableResponse response = TaskAcceptanceTask.modifyTaskTask(modifyRequest, taskId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindTaskResponseDto result = TaskAcceptanceTask.findTaskTask(taskId).as(FindTaskResponseDto.class);
        assertThat(result.getTaskTitle()).isEqualTo("수정된 테스크 제목");
    }

    @Test
    void 테스크_삭제(){

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(projectResponse.getId());
        Long sprintId = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintId);
        Long taskId = TaskAcceptanceTask.registerTaskTask(taskRequest).as(CommandSuccessResponse.Created.class).getId();

        //테스크 삭제
        //when
        ExtractableResponse response = TaskAcceptanceTask.removeTaskTask(taskId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(taskRepository.findById(taskId)).isEmpty();
    }

    @Test
    void 테스크_할당(){

        //직접 생성하기 위해 사전에 저장한 내용을 삭제
        memberRepository.deleteAll();

        //멤버 생성
        RegisterMemberRequest admin = RegisterMemberRequestFactory.createAdmin();
        RegisterMemberRequest member = RegisterMemberRequestFactory.createMember();

        Long adminId = MemberAcceptanceTask.registerMemberTask(admin, emailRepository).as(CommandSuccessResponse.Created.class).getId();
        Long memberId = MemberAcceptanceTask.registerMemberTask(member, emailRepository).as(CommandSuccessResponse.Created.class).getId();

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);
        Project project = projectRepository.findById(projectResponse.getId()).get();

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        CommandSuccessResponse.Created sprintResponse = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class);

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintResponse.getId());
        CommandSuccessResponse.Created taskResponse = TaskAcceptanceTask.registerTaskTask(taskRequest).as(CommandSuccessResponse.Created.class);

        //when
        //관리자 테스크 할당
        ExtractableResponse response = TaskAcceptanceTask.assignTaskTask(taskResponse.getId(), adminId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        QueueInformation queueInfo1 = amqpAdmin.getQueueInfo("user-" + adminId);
        assertThat(queueInfo1.getMessageCount()).isEqualTo(1); //작업 할당 메시지 1개

        QueueInformation queueInfo2 = amqpAdmin.getQueueInfo("user-" + memberId);
        assertThat(queueInfo2.getMessageCount()).isEqualTo(1); //프로젝트 가입으로 인한 Viewer 권한 메시지 1개
    }

    @Test
    void 테스크_작업상태_수정(){

        //직접 생성하기 위해 사전에 저장한 내용을 삭제
        memberRepository.deleteAll();

        //멤버 생성
        RegisterMemberRequest admin = RegisterMemberRequestFactory.createAdmin();
        RegisterMemberRequest member = RegisterMemberRequestFactory.createMember();

        Long adminId = MemberAcceptanceTask.registerMemberTask(admin, emailRepository).as(CommandSuccessResponse.Created.class).getId();
        Long memberId = MemberAcceptanceTask.registerMemberTask(member, emailRepository).as(CommandSuccessResponse.Created.class).getId();

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);
        Project project = projectRepository.findById(projectResponse.getId()).get();

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        CommandSuccessResponse.Created sprintResponse = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class);

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintResponse.getId());
        CommandSuccessResponse.Created taskResponse = TaskAcceptanceTask.registerTaskTask(taskRequest).as(CommandSuccessResponse.Created.class);

        //테스크 할당
        TaskAcceptanceTask.assignTaskTask(taskResponse.getId(), adminId);

        //when
        //테스크 작업 상태 변경
        ChangeWorkStatusRequest request = new ChangeWorkStatusRequest("complete");
        ExtractableResponse response = TaskAcceptanceTask.changeWorkStatusTaskTask(taskResponse.getId(), request);

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindTaskResponseDto result = TaskAcceptanceTask.findTaskTask(taskResponse.getId()).as(FindTaskResponseDto.class);
        assertThat(result.getWorkStatus()).isEqualTo(WorkStatus.COMPLETE);
    }

    @Test
    void 테스크_조회(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);
        Project project = projectRepository.findById(projectResponse.getId()).get();

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        CommandSuccessResponse.Created sprintResponse = SprintAcceptanceTask.registerSprintTask(sprintRequest).as(CommandSuccessResponse.Created.class);

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprintResponse.getId());
        CommandSuccessResponse.Created taskResponse = TaskAcceptanceTask.registerTaskTask(taskRequest).as(CommandSuccessResponse.Created.class);

        //테스크 조회
        //when
        ExtractableResponse response = TaskAcceptanceTask.findTaskTask(taskResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
