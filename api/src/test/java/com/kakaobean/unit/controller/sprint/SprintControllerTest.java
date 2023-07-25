package com.kakaobean.unit.controller.sprint;

import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.sprint.ModifySprintRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
                        fieldWithPath("title").type(STRING).description("스프린트 제목"),
                        fieldWithPath("description").type(STRING).description("스프린트 본문"),
                        fieldWithPath("projectId").type(NUMBER).description("스프린트 프로젝트 id"),
                        fieldWithPath("startDate").type(STRING).description("스프린트 시작 날짜"),
                        fieldWithPath("endDate").type(STRING).description("스프린트 마감 날짜")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("스프린트가 생성되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 스프린트_수정() throws Exception {
        // given
        ModifySprintRequest request = ModifySprintRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(patch("/sprints/{sprintId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_sprint",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("sprintId").description("수정할 스프린트의 id")
                ),
                requestFields(
                        fieldWithPath("newTitle").type(STRING).description("수정된 스프린트 제목"),
                        fieldWithPath("newDescription").type(STRING).description("수정된 스프린트 본문"),
                        fieldWithPath("newStartDate").type(STRING).description("수정된 스프린트 시작 날짜"),
                        fieldWithPath("newEndDate").type(STRING).description("수정된 스프린트 마감 날짜")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("스프린트가 수정되었습니다.")
                )
        ));
    }

}
