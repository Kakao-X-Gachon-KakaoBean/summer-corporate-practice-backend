package com.kakaobean.acceptance.auth;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.auth.AuthAcceptanceTask;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.auth.dto.GetAccessTokenRequest;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.fixture.member.MemberFactory;
import com.kakaobean.security.local.LocalLoginRequest;
import com.kakaobean.security.local.LocalLoginResponse;
import com.kakaobean.fixture.member.RegisterMemberRequestFactory;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;


import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


@ExtendWith({RestDocumentationExtension.class})
public class AuthAcceptanceTest extends AcceptanceTest {

    private RequestSpecification spec;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void login(){

        //given
        Member memberFixture = MemberFactory.createAdminWithTempEmail();
        MemberAcceptanceTask.registerMemberTask(RegisterMemberRequestFactory.createMember(memberFixture), emailRepository);

        LocalLoginRequest loginRequest = new LocalLoginRequest(memberFixture.getAuth().getEmail(), memberFixture.getAuth().getPassword());

        //when
        ExtractableResponse response = AuthAcceptanceTask.login(loginRequest, spec);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 리프레쉬_토큰을_사용해_액세스_토큰을_발급받는다() {

        //given
        Member memberFixture = MemberFactory.createAdminWithTempEmail();
        MemberAcceptanceTask.registerMemberTask(RegisterMemberRequestFactory.createMember(memberFixture), emailRepository);

        LocalLoginRequest loginRequest = new LocalLoginRequest(memberFixture.getAuth().getEmail(), memberFixture.getAuth().getPassword());
        ExtractableResponse givenResponse1 = AuthAcceptanceTask.login(loginRequest, spec);
        LocalLoginResponse givenResponse2 = givenResponse1.as(LocalLoginResponse.class);

        GetAccessTokenRequest request = new GetAccessTokenRequest(givenResponse2.getRefreshToken());

        //when
        ExtractableResponse response = AuthAcceptanceTask.getAccessTokenTask(request, spec);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }
}
