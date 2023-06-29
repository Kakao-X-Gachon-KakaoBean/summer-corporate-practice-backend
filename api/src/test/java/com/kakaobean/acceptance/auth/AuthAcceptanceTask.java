package com.kakaobean.acceptance.auth;

import com.kakaobean.security.local.LocalLoginRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

public class AuthAcceptanceTask {

    private AuthAcceptanceTask(){}

    static public ExtractableResponse login(LocalLoginRequest request, RequestSpecification spec){
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
                                fieldWithPath("tokenType").type(STRING).description("토큰 타입")
                        )
                ))
                .when().post("/local/login")
                .then().log().all()
                .extract();
    }
}
