package com.kakaobean.acceptance.project;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.*;

import com.kakaobean.project.dto.request.RegisterProjectRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ProjectAcceptanceTask {

    private ProjectAcceptanceTask(){}

    static public ExtractableResponse registerProjectTask(RegisterProjectRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/projects")
                .then().log().all()
                .extract();
    }
}
