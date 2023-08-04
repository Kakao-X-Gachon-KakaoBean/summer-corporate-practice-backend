package com.kakaobean.acceptance.issue;

import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommentAcceptanceTask {

    private CommentAcceptanceTask() {}

    public static ExtractableResponse registerCommentTask(RegisterCommentRequest request, Long issueId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/issues/{issueId}/comments", issueId)
                .then().log().all()
                .extract();
    }
}
