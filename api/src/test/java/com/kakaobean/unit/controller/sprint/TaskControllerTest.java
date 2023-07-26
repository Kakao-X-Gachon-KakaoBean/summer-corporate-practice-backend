package com.kakaobean.unit.controller.sprint;

import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.sprint.ModifyTaskRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterTaskRequestFactory;
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

public class TaskControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 테스크_생성() throws Exception {
        // given
        RegisterTaskRequest request = RegisterTaskRequestFactory.create();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_tasks",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("테스크 제목"),
                        fieldWithPath("content").type(STRING).description("테스크 본문"),
                        fieldWithPath("sprintId").type(NUMBER).description("테스크의 스프린트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("테스크가 생성 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 테스크_수정() throws Exception {
        // given
        ModifyTaskRequest request = ModifyTaskRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(patch("/tasks/{taskId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_tasks",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("taskId").description("수정된 테스크의 id")
                ),
                requestFields(
                        fieldWithPath("newTitle").type(STRING).description("수정된 테스크 제목"),
                        fieldWithPath("newContent").type(STRING).description("수정된 테스크 본문"),
                        fieldWithPath("sprintId").type(NUMBER).description("수정된 테스크의 스프린트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("테스크가 수정 되었습니다.")
                )
        ));
    }
}
