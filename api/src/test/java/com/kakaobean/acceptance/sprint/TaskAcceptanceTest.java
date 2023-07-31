package com.kakaobean.acceptance.sprint;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.acceptance.releasenote.ReleaseNoteAcceptanceTask;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.Task;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.ModifyTaskRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterTaskRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.ADMIN;
import static com.kakaobean.acceptance.TestMember.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    AmqpAdmin amqpAdmin;

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

    @Test
    void 테스크_할당(){

        //직접 생성하기 위해 사전에 저장한 내용을 삭제
        memberRepository.deleteAll();

        //멤버 생성
        RegisterMemberRequest request1 = RegisterMemberRequestFactory.createRequest();
        RegisterMemberRequest request2 = RegisterMemberRequestFactory.createRequestV2();

        MemberAcceptanceTask.registerMemberTask(request1, emailRepository);
        MemberAcceptanceTask.registerMemberTask(request2, emailRepository);

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //스프린트 생성
        RegisterSprintRequest sprintRequest = RegisterSprintRequestFactory.createWithId(project.getId());
        SprintAcceptanceTask.registerSprintTask(sprintRequest);
        Sprint sprint = sprintRepository.findAll().get(0);

        //테스크 생성
        RegisterTaskRequest taskRequest = RegisterTaskRequestFactory.createWithId(sprint.getId());
        TaskAcceptanceTask.registerTaskTask(taskRequest);
        Task task = taskRepository.findAll().get(0);

        //when
        //테스크 할당
        Long workerId = projectMemberRepository.findAll().get(0).getMemberId();
        ExtractableResponse response = TaskAcceptanceTask.assignTask(task.getId(), workerId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(notificationRepository.findAll().size()).isEqualTo(2);

        Long adminId = memberRepository.findMemberByEmail(ADMIN.getEmail()).get().getId();
        QueueInformation queueInfo1 = amqpAdmin.getQueueInfo("user-" + adminId);
        assertThat(queueInfo1.getMessageCount()).isEqualTo(1); //작업 할당 메시지 1개

        Long memberId = memberRepository.findMemberByEmail(MEMBER.getEmail()).get().getId();
        QueueInformation queueInfo2 = amqpAdmin.getQueueInfo("user-" + memberId);
        assertThat(queueInfo2.getMessageCount()).isEqualTo(1); //프로젝트 가입으로 인한 Viewer 권한 메시지 1개
    }
}
