package com.kakaobean.acceptance.releasenote;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.MEMBER;
import static com.kakaobean.acceptance.TestMember.RECEIVER;
import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseNoteAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    MemberRepository memberRepository;

    /**
     * 1. 릴리즈 노트를 배포한다.
     * 2. 모든 멤버에게 배포 성공 알림(이메일, amqp 메시지)이 성공적으로 가야한다.
     */
    @Test
    void 릴리즈_노트를_배포한다(){

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

        //릴리즈 노트 배포 요창
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", 1.1, project.getId());

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(notificationRepository.findAll().size()).isEqualTo(2);
    }
}