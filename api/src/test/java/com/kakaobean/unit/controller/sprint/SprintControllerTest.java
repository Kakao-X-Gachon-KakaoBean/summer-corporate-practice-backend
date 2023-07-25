package com.kakaobean.unit.controller.sprint;

import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SprintControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 스프린트_생성() throws Exception {
        // given
        RegisterSprintRequest request = RegisterSprintRequestFactory.create();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_sprint",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("스프린트 노트 제목"),
                        fieldWithPath("description").type(STRING).description("스프린트 노트 본문"),
                        fieldWithPath("projectId").type(NUMBER).description("스프린트 프로젝트 id"),
                        fieldWithPath("startDate").type(STRING).description("스프린트 시작 날짜"),
                        fieldWithPath("endDate").type(STRING).description("스프린트 마감 날짜")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("스프린트가 생성되었습니다.")
                )
        ));
    }

}
