package com.kakaobean.acceptance.releasenote;

import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getAdminAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ReleaseNoteAcceptanceTask {

    private ReleaseNoteAcceptanceTask(){}

    public static ExtractableResponse deployReleaseNoteTask(DeployReleaseNoteRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/release-notes")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findReleaseNotesTask(Long projectId, Integer page){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .param("projectId", projectId)
                .param("page", page)
                .when()
                .get("/release-notes/page")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findReleaseNoteTask(Long releaseNoteId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .get("/release-notes/{releasenoteId}", releaseNoteId)
                .then().log().all()
                .extract();
    }
}
