package com.kakaobean.acceptance.releasenote;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.auth.AuthAcceptanceTask;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.acceptance.project.ProjectAcceptanceTask;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptsResponseDto;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.releasenote.dto.request.ModifyManuscriptRequest;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class ManuscriptAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ManuscriptRepository manuscriptRepository;

    /**
     * 관리자가 릴리즈 노트를 정상적으로 생성하는 것이 목표
     */
    @Test
    void 릴리즈_노트_원고를_생성한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        RegisterManuscriptRequest request = new RegisterManuscriptRequest("1.1V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());

        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.registerManuscriptTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(manuscriptRepository.findAll().size()).isEqualTo(1);
    }


    @Test
    void 릴리즈_노트_원고_1개를_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        RegisterManuscriptRequest request = new RegisterManuscriptRequest("1.1V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ManuscriptAcceptanceTask.registerManuscriptTask(request);
        Manuscript manuscript = manuscriptRepository.findAll().get(0);

        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.findManuscriptTask(manuscript.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 릴리즈_노트_원고_10개_이하를_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        RegisterManuscriptRequest request = new RegisterManuscriptRequest("1.1V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        RegisterManuscriptRequest request2 = new RegisterManuscriptRequest("1.2V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.2", project.getId());

        ManuscriptAcceptanceTask.registerManuscriptTask(request);
        ManuscriptAcceptanceTask.registerManuscriptTask(request2);
        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.findManuscriptsTask(project.getId(), 0);

        //then
        FindManuscriptsResponseDto dto = response.as(FindManuscriptsResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(dto.isFinalPage()).isTrue();
    }

    @Test
    void 릴리즈_노트_원고_10개_이상을_페이징을_사용해_조회한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        for (int i = 1; i < 15; i++) {
            RegisterManuscriptRequest request = new RegisterManuscriptRequest("1." + i + "v 노트" , ".. 배포 내용", "1." + i, project.getId());
            ManuscriptAcceptanceTask.registerManuscriptTask(request);
        }
        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.findManuscriptsTask(project.getId(), 0);

        //then
        FindManuscriptsResponseDto dto = response.as(FindManuscriptsResponseDto.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(dto.isFinalPage()).isFalse();
        assertThat(dto.getManuscripts().size()).isEqualTo(10);
    }


    @Test
    void 릴리즈_노트_원고_수정_권한을_획득한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        RegisterManuscriptRequest request = new RegisterManuscriptRequest("1.9V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ManuscriptAcceptanceTask.registerManuscriptTask(request);

        Long manuscriptId = manuscriptRepository.findAll().get(0).getId();

        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.hasRightToModifyManuscriptTask(manuscriptId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 릴리즈_노트_원고를_수정한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        RegisterManuscriptRequest givenRequest2 = new RegisterManuscriptRequest("1.9V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ManuscriptAcceptanceTask.registerManuscriptTask(givenRequest2);

        //권한을 얻는다.
        Long manuscriptId = manuscriptRepository.findAll().get(0).getId();
        ManuscriptAcceptanceTask.hasRightToModifyManuscriptTask(manuscriptId);
        ModifyManuscriptRequest request = new ModifyManuscriptRequest("1.9.1V 코코노트 초기 릴리즈 노트", "수정된 배포 내용", "1.9.1V");

        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.modifyManuscriptTask(request, manuscriptId);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    void 릴리즈_노트_원고를_삭제한다(){

        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        //릴리즈 노트 원고 생성
        RegisterManuscriptRequest givenRequest2 = new RegisterManuscriptRequest("1.9V 코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ManuscriptAcceptanceTask.registerManuscriptTask(givenRequest2);

        Long id = manuscriptRepository.findAll().get(0).getId();

        //when
        ExtractableResponse response = ManuscriptAcceptanceTask.deleteManuscriptTask(id);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}