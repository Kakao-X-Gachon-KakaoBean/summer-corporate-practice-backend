package com.kakaobean.acceptance.project;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.releasenote.ReleaseNoteAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.domain.repository.query.FindProjectInfoResponseDto;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.ModifyProjectRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import io.lettuce.core.protocol.Command;
import io.restassured.response.ExtractableResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static com.kakaobean.acceptance.TestMember.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProjectAcceptanceTest extends AcceptanceTest {



    @Test
    void 프로젝트를_만든다(){

        //then
        RegisterProjectRequest request = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");

        ExtractableResponse response = ProjectAcceptanceTask.registerProjectTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);

        CommandSuccessResponse.Created result = response.as(CommandSuccessResponse.Created.class);
        FindProjectInfoResponseDto projectInfo = ProjectAcceptanceTask.findProjectInfoTask(result.getId()).as(FindProjectInfoResponseDto.class);
        assertThat(projectInfo).isNotNull();
    }

    @Test
    void 프로젝트에_멤버를_초대한다(){

        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);
        InviteProjectMemberRequest request = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));

        //when
        ExtractableResponse response = ProjectAcceptanceTask.inviteProjectMemberTask(request, projectResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 초대받은_멤버가_프로젝트에_가입한다(){

        //given
        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);

        //프로젝트 초대
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, projectResponse.getId());

        //프로젝트 가입
        Project project = projectRepository.findById(projectResponse.getId()).get(); //secretKey를 찾기위해 어쩔 수 없이 사용
        RegisterProjectMemberRequest request = new RegisterProjectMemberRequest(project.getSecretKey());

        //when
        ExtractableResponse response = ProjectAcceptanceTask.joinProjectMemberTask(request);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        FindProjectInfoResponseDto projectInfo = ProjectAcceptanceTask.findProjectInfoTask(projectResponse.getId()).as(FindProjectInfoResponseDto.class);
        assertThat(projectInfo.getProjectMembers().size()).isEqualTo(2);
    }

    @Test
    void 어드민이_프로젝트_정보를_변경한다(){
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);

        ModifyProjectRequest request = new ModifyProjectRequest("새로운 프로젝트 제목", "새로운 프로젝트 내용");

        //when
        ExtractableResponse response = ProjectAcceptanceTask.modifyProjectInfoTask(request, projectResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        FindProjectInfoResponseDto projectInfo = ProjectAcceptanceTask.findProjectInfoTask(projectResponse.getId()).as(FindProjectInfoResponseDto.class);
        assertThat(projectInfo.getProjectTitle()).isEqualTo("새로운 프로젝트 제목");
    }

    // 비동기 테스트 적용X
    @Test
    @Rollback(value = false)
    void 어드민이_프로젝트를_삭제한다(){
        //given
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);

        DeployReleaseNoteRequest request = new DeployReleaseNoteRequest("코코노트 초기 릴리즈 노트", ".. 배포 내용", "1.1", projectResponse.getId());
        ReleaseNoteAcceptanceTask.deployReleaseNoteTask(request);

        //when
        ExtractableResponse response = ProjectAcceptanceTask.removeProjectTask(projectResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);

        //조회 API가 NULL 포인터 익셉션이 나오므로 직접 조회
        assertThat(projectRepository.findById(projectResponse.getId()).isEmpty()).isTrue();
        assertThat(releaseNoteRepository.findByProjectId(projectResponse.getId()).isEmpty()).isTrue();
    }

    @Test
    void 프로젝트_전체_정보를_조회한다(){

        //given
        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);

        //프로젝트 초대
        InviteProjectMemberRequest givenDto = new InviteProjectMemberRequest(List.of(MEMBER.getEmail()));
        ProjectAcceptanceTask.inviteProjectMemberTask(givenDto, projectResponse.getId());

        //프로젝트 가입
        Project project = projectRepository.findById(projectResponse.getId()).get();
        RegisterProjectMemberRequest request = new RegisterProjectMemberRequest(project.getSecretKey());
        ProjectAcceptanceTask.joinProjectMemberTask(request);

        //when
        ExtractableResponse response = ProjectAcceptanceTask.findProjectInfoTask(project.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 프로젝트_타이틀을_조회한다(){

        //given
        //프로젝트 생성
        RegisterProjectRequest givenRequest = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");
        CommandSuccessResponse.Created projectResponse = ProjectAcceptanceTask.registerProjectTask(givenRequest).as(CommandSuccessResponse.Created.class);
        Project project = projectRepository.findById(projectResponse.getId()).get();

        //when
        ExtractableResponse response = ProjectAcceptanceTask.findProjectTitleTask(project.getSecretKey());

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}