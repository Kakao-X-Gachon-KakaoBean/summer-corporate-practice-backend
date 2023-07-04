package com.kakaobean.acceptance.project;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getMemberAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.*;

import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.project.dto.request.RegisterProjectRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ProjectAcceptanceTask {

    private ProjectAcceptanceTask(){}

    public static ExtractableResponse registerProjectTask(RegisterProjectRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/projects")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse inviteProjectMemberTask(InviteProjectMemberRequest request,
                                                              Long projectId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getMemberAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/projects/{}/members", projectId)
                .then().log().all()
                .extract();
    }

}
