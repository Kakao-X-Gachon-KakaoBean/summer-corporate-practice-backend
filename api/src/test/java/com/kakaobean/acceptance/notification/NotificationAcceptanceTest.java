package com.kakaobean.acceptance.notification;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationsResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.fixture.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationAcceptanceTest extends AcceptanceTest {


    @Test
    void 알림_조회(){

        //직접 생성하기 위해 사전에 저장한 내용을 삭제
        memberRepository.deleteAll();

        //멤버 생성
        RegisterMemberRequest request1 = RegisterMemberRequestFactory.createAdmin();
        RegisterMemberRequest request2 = RegisterMemberRequestFactory.createMember();

        MemberAcceptanceTask.registerMemberTask(request1, emailRepository);
        MemberAcceptanceTask.registerMemberTask(request2, emailRepository);

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        Project project = projectRepository.findById(projectResponse.getId()).get();

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
        RegisterMemberRequest request1 = RegisterMemberRequestFactory.createAdmin();
        RegisterMemberRequest request2 = RegisterMemberRequestFactory.createMember();

        MemberAcceptanceTask.registerMemberTask(request1, emailRepository);
        MemberAcceptanceTask.registerMemberTask(request2, emailRepository);

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        Project project = projectRepository.findById(projectResponse.getId()).get();

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //알림 조회
        //when
        ExtractableResponse response = NotificationAcceptanceTask.findNotificationsWithPagingTask(null);

        //then
        FindNotificationsResponseDto result = response.as(FindNotificationsResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.getNotifications().size()).isEqualTo(1);
    }

    @Test
    void 알림_읽기_상태_변경(){

        //직접 생성하기 위해 사전에 저장한 내용을 삭제
        memberRepository.deleteAll();

        //멤버 생성
        RegisterMemberRequest request1 = RegisterMemberRequestFactory.createAdmin();
        RegisterMemberRequest request2 = RegisterMemberRequestFactory.createMember();

        MemberAcceptanceTask.registerMemberTask(request1, emailRepository);
        MemberAcceptanceTask.registerMemberTask(request2, emailRepository);

        //프로젝트 생성
        RegisterProjectRequest projectRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(projectRequest).as(CommandSuccessResponse.Created.class);

        Project project = projectRepository.findById(projectResponse.getId()).get();

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //조회
        FindNotificationsResponseDto notifications = NotificationAcceptanceTask.findNotificationsWithPagingTask(null).as(FindNotificationsResponseDto.class);

        //when
        ExtractableResponse response = NotificationAcceptanceTask.modifyNotificationTask(notifications.getNotifications().get(0).getNotificationId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        FindNotificationsResponseDto result = NotificationAcceptanceTask.findNotificationsWithPagingTask(null).as(FindNotificationsResponseDto.class);
        assertThat(result.getNotifications().get(0).isHasRead()).isTrue();

    }
}
