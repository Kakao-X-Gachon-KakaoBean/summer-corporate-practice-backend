package com.kakaobean.acceptance.sprint;

import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TaskAcceptanceTask {

    private TaskAcceptanceTask() {}

    public static ExtractableResponse registerTaskTask(RegisterTaskRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/tasks")
                .then().log().all()
                .extract();
    }
}
