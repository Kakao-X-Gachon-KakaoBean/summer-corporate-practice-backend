package com.kakaobean.acceptance.issue;

import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class IssueAcceptanceTask {
    private IssueAcceptanceTask() {}

    public static ExtractableResponse registerIssueTask(RegisterIssueRequest request, Long projectId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/projects/{projectId}/issues", projectId)
                .then().log().all()
                .extract();
    }
}
