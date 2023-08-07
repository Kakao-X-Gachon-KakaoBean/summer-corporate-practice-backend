package com.kakaobean.acceptance.auth;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.auth.dto.GetAccessTokenRequest;
import com.kakaobean.config.AppProperties;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.security.local.LocalLoginRequest;
import com.kakaobean.security.local.LocalLoginResponse;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;


import java.util.Date;

import static com.kakaobean.acceptance.TestMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


@ExtendWith({RestDocumentationExtension.class})
public class AuthAcceptanceTest extends AcceptanceTest {

    private RequestSpecification spec;

    @Autowired
    EmailRepository emailRepository;


    @Autowired
    AppProperties appProperties;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void login(){

        //given
        MemberAcceptanceTask.registerMemberTask(RegisterMemberRequestFactory.createRequestV3(), emailRepository);
        LocalLoginRequest loginRequest = new LocalLoginRequest(TESTER.getEmail(), TESTER.getPassword());

        //when
        ExtractableResponse response = AuthAcceptanceTask.login(loginRequest, spec);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 리프레쉬_토큰을_사용해_액세스_토큰을_발급받는다() {

        //given
        MemberAcceptanceTask.registerMemberTask(RegisterMemberRequestFactory.createRequestV3(), emailRepository);
        LocalLoginRequest loginRequest = new LocalLoginRequest(TESTER.getEmail(), TESTER.getPassword());
        ExtractableResponse givenResponse1 = AuthAcceptanceTask.login(loginRequest, spec);
        LocalLoginResponse givenResponse2 = givenResponse1.as(LocalLoginResponse.class);
        GetAccessTokenRequest request = new GetAccessTokenRequest(givenResponse2.getRefreshToken());

        //when
        ExtractableResponse response = AuthAcceptanceTask.getAccessTokenTask(request, spec);

        //then
        assertThat(response.statusCode()).isEqualTo(201);
    }
}
