package com.kakaobean.acceptance.project;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.auth.AuthAcceptanceTask;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import io.restassured.response.ExtractableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProjectAcceptanceTest extends AcceptanceTest {

    @Test
    void 프로젝트를_만든다(){

        //given
        RegisterProjectRequest request = new RegisterProjectRequest("테스트 프로젝트", "테스트 프로젝트 설명");

        //when
        ExtractableResponse response = ProjectAcceptanceTask.registerProjectTask(request);

        //given
        Assertions.assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void 프로젝트에_참여한다(){

    }
}
