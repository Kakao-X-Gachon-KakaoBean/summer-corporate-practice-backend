package com.kakaobean.acceptance.notification;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

import static com.kakaobean.acceptance.auth.AuthAcceptanceTask.getMemberAuthorizationHeaderToken;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class NotificationAcceptanceTask {

    private NotificationAcceptanceTask() {}

    public static ExtractableResponse findNotificationTask(){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getMemberAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .get("/notifications")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse findNotificationsWithPagingTask(Long page){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getMemberAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .param("page", page)
                .when()
                .get("/notifications/page")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse modifyNotificationTask(Long notificationId){
        return RestAssured
                .given()
                .header(AUTHORIZATION, getMemberAuthorizationHeaderToken())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .patch("/notifications/{notificationId}", notificationId)
                .then().log().all()
                .extract();
    }
}
