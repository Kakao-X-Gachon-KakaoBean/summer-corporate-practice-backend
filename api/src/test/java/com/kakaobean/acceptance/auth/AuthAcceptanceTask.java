package com.kakaobean.acceptance.auth;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.auth.dto.GetAccessTokenRequest;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.security.local.LocalLoginRequest;
import com.kakaobean.security.local.LocalLoginResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.concurrent.atomic.AtomicInteger;

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
                                fieldWithPath("tokenType").type(STRING).description("토큰 타입"),
                                fieldWithPath("memberId").type(NUMBER).description("멤버 id")
                        )
                ))
                .when().post("/local/login")
                .then()
                .extract();
    }

    private static ExtractableResponse requestLogin(LocalLoginRequest request){
        return RestAssured
                .given()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/local/login")
                .then().log().all()
                .extract();
    }

    public static String getAdminAuthorizationHeaderToken(){
        if(AcceptanceTest.adminTokenContext.get() != null){
            String token = AcceptanceTest.adminTokenContext.get();
            return "Bearer " + token;
        }

        Member admin = AcceptanceTest.memberContext.get().getAdmin();
        ExtractableResponse response = requestLogin(new LocalLoginRequest(admin.getAuth().getEmail(), admin.getAuth().getPassword()));
        String accessToken = response.as(LocalLoginResponse.class).getAccessToken();
        AcceptanceTest.adminTokenContext.set(accessToken);
        return "Bearer " + accessToken;
    }

    public static String getMemberAuthorizationHeaderToken(){

        if(AcceptanceTest.memberTokenContext.get() != null){
            String token = AcceptanceTest.memberTokenContext.get();
            return "Bearer " + token;
        }

        Member member = AcceptanceTest.memberContext.get().getMember();
        ExtractableResponse response = requestLogin(new LocalLoginRequest(member.getAuth().getEmail(), member.getAuth().getPassword()));
        String accessToken = response.as(LocalLoginResponse.class).getAccessToken();
        AcceptanceTest.memberTokenContext.set(accessToken);
        return "Bearer " + accessToken;
    }


    public static ExtractableResponse getAccessTokenTask(GetAccessTokenRequest request, RequestSpecification spec){
        return RestAssured
                .given(spec)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .filter(document("get_access_token",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(STRING).description("리프레쉬 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(STRING).description("로그인 액세스 토큰")
                        )
                ))
                .when().post("/access-tokens")
                .then().log().all()
                .extract();
    }

}
