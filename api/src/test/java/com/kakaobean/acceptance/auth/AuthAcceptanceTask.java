package com.kakaobean.acceptance.auth;

import com.kakaobean.security.local.LocalLoginRequest;
import com.kakaobean.security.local.LocalLoginResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;

import static com.kakaobean.acceptance.TestMember.*;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

public class AuthAcceptanceTask {

    private AuthAcceptanceTask(){}

    public static ExtractableResponse login(LocalLoginRequest request, RequestSpecification spec){
        return RestAssured
                .given(spec)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .filter(document("local_login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(STRING).description("이메일"),
                                fieldWithPath("password").type(STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(STRING).description("로그인 액세스 토큰"),
                                fieldWithPath("refreshToken").type(STRING).description("로그인 리프레쉬 토큰"),
                                fieldWithPath("tokenType").type(STRING).description("토큰 타입")
                        )
                ))
                .when().post("/local/login")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse requestLogin(LocalLoginRequest request){
        return RestAssured
                .given()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/local/login")
                .then().log().all()
                .extract();
    }

    public static String getAdminAuthorizationHeaderToken(){
        ExtractableResponse response = requestLogin(new LocalLoginRequest(ADMIN.getEmail(), ADMIN.getPassword()));
        String accessToken = response.as(LocalLoginResponse.class).getAccessToken();
        return "Bearer " + accessToken;
    }

    public static String getMemberAuthorizationHeaderToken(){
        ExtractableResponse response = requestLogin(new LocalLoginRequest(MEMBER.getEmail(), MEMBER.getPassword()));
        String accessToken = response.as(LocalLoginResponse.class).getAccessToken();
        return "Bearer " + accessToken;
    }

}
