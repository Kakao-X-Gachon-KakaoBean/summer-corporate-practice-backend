package com.kakaobean.acceptance.sprint;

import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SprintAcceptanceTask {

    private SprintAcceptanceTask() {}

    public static ExtractableResponse registerSprintTask(RegisterSprintRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/sprints")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse modifySprintTask(ModifySprintRequest request, Long sprintId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/sprints/{sprintId}", sprintId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse removeSprintTask(Long sprintId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .delete("/sprints/{sprintId}", sprintId)
                .then().log().all()
                .extract();
    }
}
