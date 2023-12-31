package com.kakaobean.acceptance.releasenote;

import com.kakaobean.releasenote.dto.request.ModifyManuscriptRequest;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
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
                .get("/manuscripts/{manuscriptId}", manuscriptId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse registerManuscriptTask(RegisterManuscriptRequest request){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/manuscripts")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findManuscriptsTaskWithPaging(Long projectId, Integer page){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .param("projectId", projectId)
                .param("page", page)
                .when()
                .get("/manuscripts/page")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse hasRightToModifyManuscriptTask(Long manuscriptId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .patch("/manuscripts/{manuscriptId}/access-status", manuscriptId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse modifyManuscriptTask(ModifyManuscriptRequest request, Long manuscriptId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/manuscripts/{manuscriptId}", manuscriptId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse deleteManuscriptTask(Long manuscriptId) {
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .delete("/manuscripts/{manuscriptId}", manuscriptId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findManuscriptsTask(Long projectId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getAdminAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .param("projectId", projectId)
                .when()
                .get("/manuscripts")
                .then().log().all()
                .extract();
    }
}
