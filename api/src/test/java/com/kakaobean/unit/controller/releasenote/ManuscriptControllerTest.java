package com.kakaobean.unit.controller.releasenote;


import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.unit.controller.ControllerTest;
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

public class ManuscriptControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 릴리즈_노트_원고_작성() throws Exception {

        //given
        RegisterManuscriptRequest request = new RegisterManuscriptRequest(
                "코코노트 1.1",
                "hello",
                "1.1",
                2L
        );
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/manuscripts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_release_note_manuscript",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("릴리즈 노트 원고 제목"),
                        fieldWithPath("content").type(STRING).description("릴리즈 노트 원고 본문"),
                        fieldWithPath("version").type(STRING).description("릴리즈 노트 원고 버전"),
                        fieldWithPath("projectId").type(NUMBER).description("릴리즈 노트 원고를 포함한 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }
}
