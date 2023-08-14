package com.kakaobean.acceptance.issue;

import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class IssueAcceptanceTask {
    private IssueAcceptanceTask() {}

    public static ExtractableResponse registerIssueTask(RegisterIssueRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/issues")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse modifyIssueTask(ModifyIssueRequest request, Long issueId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/issues/{issueId}", issueId)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse removeIssueTask(Long issueId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .delete("/issues/{issueId}", issueId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findIssueWithPagingTask(Long projectId, Integer page){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .param("projectId", projectId)
                .param("page", page)
                .when()
                .get("/issues/page")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findIssueWithId(Long issueId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .get("/issues/{issueId}",issueId)
                .then().log().all()
                .extract();
    }
}
