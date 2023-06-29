package com.kakaobean.acceptance.auth;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.security.local.LocalLoginRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;


import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


@ExtendWith({RestDocumentationExtension.class})
public class AuthAcceptanceTest extends AcceptanceTest {

    private RequestSpecification spec;

    @Autowired
    EmailRepository emailRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void login(){

        //given
        MemberAcceptanceTask.registerMemberTask(RegisterMemberRequestFactory.createRequest(), emailRepository);
        LocalLoginRequest loginRequest = new LocalLoginRequest("example@gmail.com", "1q2w3e4r!");

        //when
        ExtractableResponse response = AuthAcceptanceTask.login(loginRequest, spec);

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
