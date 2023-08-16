package com.kakaobean.unit.controller.notification;

import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static com.kakaobean.unit.controller.factory.notification.FindNotificationResponseDtoFactory.create;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotificationControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 최근_알림_조회() throws Exception {
        //given
        given(notificationQueryRepository.findByMemberId(Mockito.anyLong())).willReturn(create());

        // when
        ResultActions perform = mockMvc.perform(get("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_notification",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].notificationId").type(NUMBER).description("알림 id"),
                        fieldWithPath("[].createdAt").type(STRING).description("알림 생성 날짜"),
                        fieldWithPath("[].content").type(STRING).description("알림 내용"),
                        fieldWithPath("[].url").type(STRING).description("알림 클릭시 라우팅될 url"),
                        fieldWithPath("[].hasRead").type(BOOLEAN).description("알림 여부")
                )
        ));
    }

    @Test
    @WithMockUser
    void 알림_패이징_조회() throws Exception {
        //given
        given(notificationQueryRepository.findByPaginationNoOffset(Mockito.anyLong(), Mockito.anyLong())).willReturn(create());

        // when
        ResultActions perform = mockMvc.perform(get("/notifications/page")
                .contentType(MediaType.APPLICATION_JSON)
                .param("lastNotificationId", "21")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_notification_page",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("lastNotificationId").description("마지막으로 조회된 알림 id. 만약 첫 페이지 죄회('더보기' 클릭)시 null을 주면 됨.")
                ),
                responseFields(
                        fieldWithPath("[].notificationId").type(NUMBER).description("알람 id"),
                        fieldWithPath("[].createdAt").type(STRING).description("알람 생성 날짜"),
                        fieldWithPath("[].content").type(STRING).description("알람 내용"),
                        fieldWithPath("[].url").type(STRING).description("알람 클릭시 라우팅될 url"),
                        fieldWithPath("[].hasRead").type(BOOLEAN).description("열람 여부")
                )
        ));
    }

    @Test
    @WithMockUser
    void 알림_열람_상태_변경() throws Exception {

        // when
        ResultActions perform = mockMvc.perform(patch("/notifications/{notificationId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_notification",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("notificationId").description("상태 변경할 알림 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("열람 상태 변경 성공 메시지")
                )
        ));
    }
}
