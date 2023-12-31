package com.kakaobean.acceptance.releasenote;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.notification.NotificationAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.notification.domain.repository.query.FindNotificationsResponseDto;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingReleaseNotesResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNotesResponseDto;
import com.kakaobean.fixture.member.MemberFactory;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.fixture.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;

import org.springframework.amqp.core.QueueInformation;


import java.util.List;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseNoteAcceptanceTest extends AcceptanceTest {



    /**
     * 1. 릴리즈 노트를 배포한다.
     * 2. 모든 멤버에게 배포 성공 알림(이메일, amqp 메시지)이 성공적으로 가야한다.
     */

    @Test
    void 릴리즈_노트를_배포한다(){

        //픽스쳐 멤버 삭제 및 재생성
        MemberContext removedContext = super.deleteMemberContext();

        RegisterMemberRequest admin = RegisterMemberRequestFactory.createMember(removedContext.getAdmin());
        RegisterMemberRequest member = RegisterMemberRequestFactory.createMember(removedContext.getMember());

        Long adminId = MemberAcceptanceTask.registerMemberTask(admin, emailRepository).as(CommandSuccessResponse.Created.class).getId();
        Long memberId = MemberAcceptanceTask.registerMemberTask(member, emailRepository).as(CommandSuccessResponse.Created.class).getId();

        Member newAdmin = MemberFactory.createWithId(adminId, removedContext.getAdmin());
        Member newMember = MemberFactory.createWithId(memberId, removedContext.getMember());

        super.setMemberContext(new MemberContext(newAdmin, newMember));

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);

        //프로젝트 멤버 가입
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(newMember.getAuth().getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, projectResponse.getId());

        Project project = projectRepository.findById(projectResponse.getId()).get();
        ProjectAcceptanceTask.joinProjectMemberTask(new RegisterProjectMemberRequest(project.getSecretKey()));

        //릴리즈 노트 배포 요창
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);

        FindNotificationsResponseDto result = NotificationAcceptanceTask.findNotificationsWithPagingTask(null).as(FindNotificationsResponseDto.class);
        assertThat(result.getNotifications().size()).isEqualTo(2);

        QueueInformation queueInfo1 = amqpAdmin.getQueueInfo("user-" + adminId);
        assertThat(queueInfo1.getMessageCount()).isEqualTo(1);

        QueueInformation queueInfo2 = amqpAdmin.getQueueInfo("user-" + memberId);
        assertThat(queueInfo2.getMessageCount()).isEqualTo(2); //초대를 받았으므로 메시지가 2개다.
    }

    @Test
    void 릴리즈_노트_10개_이하를_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);


        //릴리즈 노트 배포 요청
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", projectResponse.getId());
        ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNotesTaskWithPaging(projectResponse.getId(), 0);

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
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);


        //릴리즈 노트 배포 요창
        for (int i = 1; i < 15; i++) {
            releaseNoteRepository.save(new ReleaseNote(ACTIVE, "1." + i  + "V title", "content", "1." + i + "V", projectResponse.getId(), 2L));
        }

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNotesTaskWithPaging(projectResponse.getId(), 0);

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
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);


        //릴리즈 노트
        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", projectResponse.getId());
        CommandSuccessResponse.Created releaseNoteResponse = ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request).as(CommandSuccessResponse.Created.class);

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNoteTask(releaseNoteResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    void 릴리즈_노트를_전체_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);

        //릴리즈 노트 배포 요창
        for (int i = 1; i < 15; i++) {
            releaseNoteRepository.save(new ReleaseNote(ACTIVE, "1." + i  + "V title", "content", "1." + i + "V", projectResponse.getId(), 1L));
        }

        //when
        ExtractableResponse response = ReleaseNoteAcceptanceTask.findReleaseNotesTask(projectResponse.getId());

        //then
        FindReleaseNotesResponseDto result = response.as(FindReleaseNotesResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(result.getReleaseNotes().size()).isEqualTo(14);
    }
}