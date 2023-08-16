package com.kakaobean.acceptance.notification;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.notification.domain.Notification;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Test
    void 알림_조회(){

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

        //알림 조회
        //when
        ExtractableResponse response = NotificationAcceptanceTask.findNotificationTask();

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 알림_첫페이지_조회(){

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

        //알림 조회
        //when
        ExtractableResponse response = NotificationAcceptanceTask.findNotificationsWithPagingTask(null);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 알림_읽기_상태_변경(){

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

        Notification notification = notificationRepository.findAll().get(0);

        //when
        ExtractableResponse response = NotificationAcceptanceTask.modifyNotificationTask(notification.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
