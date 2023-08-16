package com.kakaobean.acceptance.releasenote;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingReleaseNotesResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNotesResponseDto;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.*;
import static com.kakaobean.core.common.domain.BaseStatus.*;
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

    @Autowired
    ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    AmqpAdmin amqpAdmin;


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
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(notificationRepository.findAll().size()).isEqualTo(3);

        Long adminId = memberRepository.findMemberByEmail(ADMIN.getEmail()).get().getId();
        QueueInformation queueInfo1 = amqpAdmin.getQueueInfo("user-" + adminId);
        assertThat(queueInfo1.getMessageCount()).isEqualTo(1);

        Long memberId = memberRepository.findMemberByEmail(MEMBER.getEmail()).get().getId();
        QueueInformation queueInfo2 = amqpAdmin.getQueueInfo("user-" + memberId);
        assertThat(queueInfo2.getMessageCount()).isEqualTo(2); //초대를 받았으므로 메시지가 2개다.
    }

    @Test
    void 릴리즈_노트_10개_이하를_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 배포 요청
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNotesTaskWithPaging(project.getId(), 0);

        //then
        FindPagingReleaseNotesResponseDto result = response.as(FindPagingReleaseNotesResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.isFinalPage()).isTrue();
        assertThat(result.getReleaseNotes().size()).isEqualTo(1);
    }

    @Test
    void 릴리즈_노트_10개_이상을_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 배포 요창
        for (int i = 1; i < 15; i++) {
            releaseNoteRepository.save(new ReleaseNote(ACTIVE, "1." + i  + "V title", "content", "1." + i + "V", project.getId(), 1L));
        }

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNotesTaskWithPaging(project.getId(), 0);

        //then
        FindPagingReleaseNotesResponseDto result = response.as(FindPagingReleaseNotesResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.isFinalPage()).isFalse();
        assertThat(result.getReleaseNotes().size()).isEqualTo(10);
    }

    @Test
    void 릴리즈_노트_단건_을_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        Long releaseNoteId = releaseNoteRepository.findAll().get(0).getId();

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNoteTask(releaseNoteId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    void 릴리즈_노트를_전체_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 배포 요창
        for (int i = 1; i < 15; i++) {
            releaseNoteRepository.save(new ReleaseNote(ACTIVE, "1." + i  + "V title", "content", "1." + i + "V", project.getId(), 1L));
        }

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNotesTask(project.getId());

        //then
        FindReleaseNotesResponseDto result = response.as(FindReleaseNotesResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.getReleaseNotes().size()).isEqualTo(14);
    }
}