package com.kakaobean.acceptance.issue;

import com.kakaobean.issue.dto.RegisterCommentRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommentAcceptanceTask {

    private CommentAcceptanceTask() {}

    public static ExtractableResponse registerCommentTask(RegisterCommentRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/comments")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse deleteCommentTask(Long commentId) {
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .delete("/comments/{commentId}", commentId)
                .then().log().all()
                .extract();
    }
}
