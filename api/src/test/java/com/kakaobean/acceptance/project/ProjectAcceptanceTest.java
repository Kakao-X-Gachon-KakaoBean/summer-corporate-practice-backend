package com.kakaobean.acceptance.project;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.releasenote.ReleaseNoteAcceptanceTask;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.ModifyProjectInfoRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import io.restassured.response.ExtractableResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProjectAcceptanceTest extends AcceptanceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ReleaseNoteRepository releaseNoteRepository;

    @Test
    void 프로젝트를_만든다(){

        //then
        RegisterProjectRequest request = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");

        ExtractableResponse response = ProjectAcceptanceTask.registerProjectTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void 프로젝트에_멤버를_초대한다(){

        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);
        InviteProjectMemberRequest request = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));

        //when
        ExtractableResponse response = ProjectAcceptanceTask.inviteProjectMemberTask(request, project.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 초대받은_멤버가_프로젝트에_가입한다(){
        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, project.getId());
        RegisterProjectMemberRequest request = new RegisterProjectMemberRequest(project.getSecretKey());

        //when
        ExtractableResponse response = ProjectAcceptanceTask.joinProjectMemberTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void 어드민이_프로젝트_정보를_변경한다(){
        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);
        ModifyProjectInfoRequest request = new ModifyProjectInfoRequest("새로운 프로젝트 제목", "새로운 프로젝트 내용");

        //when
        ExtractableResponse response = ProjectAcceptanceTask.modifyProjectInfoTask(request, project.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @Rollback(value = false)
    void 어드민이_프로젝트를_삭제한다(){
        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        ProjectAcceptanceTask.registerProjectTask(givenRequest);
        Project project = projectRepository.findAll().get(0);

        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", project.getId());
        ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);
        ReleaseNote releaseNote = releaseNoteRepository.findByProjectId(project.getId()).get(0);

        //when
        ExtractableResponse response = ProjectAcceptanceTask.removeProjectTask(project.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(projectRepository.findAll().size()).isEqualTo(0);
        assertThat(projectMemberRepository.findAll().size()).isEqualTo(0);
        assertThat(releaseNoteRepository.findAll().size()).isEqualTo(0);
    }
}

/**
 * 클래스에 @BeforeEach가 있으면
 * 부모 클래스에 @BeforeEach가 실행되지 않음.
 */