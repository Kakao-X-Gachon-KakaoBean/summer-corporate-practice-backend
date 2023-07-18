package com.kakaobean.acceptance.releasenote;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ManuscriptAcceptanceTask {

    private ManuscriptAcceptanceTask(){}

    public static ExtractableResponse findManuscriptTask(Long manuscriptId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/manuscript/{manuscriptId}", manuscriptId)
                .then().log().all()
                .extract();
    }
}
