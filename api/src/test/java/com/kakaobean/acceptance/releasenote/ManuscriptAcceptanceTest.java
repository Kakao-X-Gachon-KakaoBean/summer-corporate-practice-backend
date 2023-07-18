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
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
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
    void 릴리즈_노트_원고를_조회한다(){

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
        assertThat(response.statusCode()).isEqualTo(201);
        FindManuscriptResponseDto result = response.as(FindManuscriptResponseDto.class);
        System.out.println("result = " + result);
    }
}